package one.kafe.kafeservice.service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.kafe.kafeservice.exception.PermissionDeniedException;
import one.kafe.kafeservice.exception.SearchResultNotExistException;
import one.kafe.kafeservice.repository.EsIndexRepository;
import one.kafe.kafeservice.repository.SUrlRepository;
import one.kafe.kafeservice.type.dto.Coordinates;
import one.kafe.kafeservice.type.entity.EsIndex;
import one.kafe.kafeservice.type.entity.SUrl;
import one.kafe.kafeservice.type.vo.StatisticsVo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
	private final EsIndexRepository esIndexRepository;

	private final SUrlRepository sUrlRepository;
	private final RestHighLevelClient client;

	@Value("${elasticsearch.indices.webIndexName}")
	private String idxName;

	/**
	 * Index 에 있는 모든 데이터 추출 - 생성해둔 Class 기반
	 */
	@Override
	public List<EsIndex> getSearchAll() {
		List<EsIndex> result = new ArrayList<>();

		Iterator<EsIndex> iterator = esIndexRepository.findAll().iterator();
		while (iterator.hasNext()) {
			result.add(iterator.next());
		}

		return result;
	}

	/** 특정 URL 을 통한 조회 - 전체 정보 조회 */
	@Override
	public List<EsIndex> findByUrl(String url) {
		return esIndexRepository.findByUrl(url);
	}

	/** 해당 URL 이 사용된 횟수를 확인 가능 */
	@Override
	public Long getUrlCnt(String url) {
		return esIndexRepository.getUrlCnt(url);
	}

	/** 통계 데이터 추출 (국가명, 기기, 브라우저, 레퍼러, 좌표, 시간) */
	@Override
	public StatisticsVo statistic(Long seq, Long userSeq) throws
		IOException,
		SearchResultNotExistException,
		PermissionDeniedException {
		Optional<SUrl> sUrl = sUrlRepository.findByIdWithUser(seq);
		if (sUrl.isEmpty()) {
			throw new SearchResultNotExistException();
		}
		SUrl result = sUrl.get();
		if (result.getUser().getSeq() != userSeq) {
			throw new PermissionDeniedException();
		}

		String url = result.getShortUrl();

		List<Map<String, Long>> useragent = getDeviceSns(url);

		Map<String, Long> browseList = useragent.get(0);
		Map<String, Long> snsList = useragent.get(1);

		StatisticsVo vo = StatisticsVo.builder()
			//			.countryName(searchByKeyword(url, "geoip.country_name.keyword")) // 나라 이름
			.device(searchByKeyword(url, "useragent.os.keyword")) // 사용자 기기 (Window, Mac, iPhone, Android)
			.useragent(browseList) // 브라우저 (Chrome, Edge, Safari, Firefox, IE)
			.httpReferer(searchByKeyword(url, "Referer.keyword")) // HTTP Referer
			.coordinates(getPoint("/" + url)) // 위도, 경도
			.timestamp(getTimestamp("/" + url))
			.sns(snsList) // SNS (Instagram, Facebook, Twitter, Kakaotalk)
			.build(); // timestamp

		return vo;
	}

	/** 특정 필드에 대한 값, 개수 조회 */
	private Map<String, Long> searchByKeyword(String url, String key) throws IOException {
		//      0. Terms Aggregation 설정 : 분류 기준 잡기
		TermsAggregationBuilder termsAgg = AggregationBuilders.terms("by_keyword").field(key);

		//      0. Query 설정
		//      분류하고자 하는 url 을 통해 우선적인 데이터 추출 하고자 함
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
			.must(QueryBuilders.matchQuery("current_path", url));

		//      1. SearchSourceBuilder 설정
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(queryBuilder);
		sourceBuilder.aggregation(termsAgg);

		//      2. SearchRequest 설정
		//      미리 설정해둔 1차 데이터 검색을 위한 쿼리 지정과 이를 분류할 기준에 대해서 설정해 줌
		SearchRequest searchRequest = new SearchRequest(idxName);
		searchRequest.source(sourceBuilder);

		//      3. SearchResponse 설정
		//      Dev Tools 에서 쿼리 작성 후 얻는 결과물과 동일한 결과를 SearchResponse 를 통해 얻게 됨
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		Terms test = response.getAggregations().get("by_keyword");

		//      4. 결과를 HashMap 에 담아서 전달
		Map<String, Long> result = new HashMap<>();

		for (Terms.Bucket entry : test.getBuckets()) {
			result.put(entry.getKey().toString(), entry.getDocCount());
		}

		return result;
	}

	/** 좌표만 조회 */
	private String getPoint(String url) {
		List<EsIndex> list = esIndexRepository.findByUrl(url);
		Map<String, Coordinates> result = new HashMap<>();

		for (EsIndex document : list) {
			JSONObject parser = new JSONObject((Map)document.getGeoIP());
			try {
				String country = parser.getString("country_name");

				float lon = parser.getJSONObject("location").getFloat("lon");
				float lat = parser.getJSONObject("location").getFloat("lat");

				Coordinates tmp = result.getOrDefault(country, new Coordinates(lon, lat, 0L));
				tmp.increaseCnt();
				result.put(country, tmp);

			} catch (JSONException ex) {
				throw new RuntimeException(ex);
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("country,lon,lat,cnt").append("\n");

		for (Map.Entry<String, Coordinates> entrySet : result.entrySet()) {
			sb.append(entrySet.getKey()).append(",").append(entrySet.getValue().getLon()).append(",")
				.append(entrySet.getValue().getLat()).append(",").append(entrySet.getValue().getCnt()).append("\n");
		}

		return sb.toString();
	}

	/** Timestamp 만 조회 */
	private List<String> getTimestamp(String url) {
		List<EsIndex> list = esIndexRepository.findByUrl(url);
		List<String> result = new ArrayList<>();

		for (EsIndex document : list) {
			StringBuilder sb = new StringBuilder();

			sb.append(document.getLocaldate().toString().substring(0, 10)).append(' ')
				.append(document.getLocaltime().toString().substring(0, 2));

			result.add(sb.toString());
		}

		return result;
	}

	private List<Map<String, Long>> getDeviceSns(String url) throws IOException {
		List<Map<String, Long>> result = new ArrayList<>();
		String[] snsList = {"Facebook", "Instagram", "Twitter", "Kakaotalk"};

		Map<String, Long> useragent = searchByKeyword(url, "useragent.name.keyword");
		Map<String, Long> mySns = new HashMap<>();

		for (String sns : snsList) {
			if (useragent.containsKey(sns)) {
				mySns.put(sns, useragent.get(sns));
				useragent.remove(sns);
			}
		}

		result.add(useragent);
		result.add(mySns);

		return result;
	}

	@Override
	public Map<String, Map<String, Long>> weeklyReport(String url, LocalDateTime startDate, LocalDateTime endDate) throws IOException {
		// 집계 기준
		TermsAggregationBuilder termsAgg1 = AggregationBuilders.terms("by_os")
				.field("useragent.os.keyword"); // 사용자 기기 (Window, Mac, iPhone, Android)
		TermsAggregationBuilder termsAgg2 = AggregationBuilders.terms("by_useragent")
				.field("useragent.name.keyword"); // 브라우저, SNS
		TermsAggregationBuilder termsAgg3 = AggregationBuilders.terms("by_date")
				.field("currentDate"); // 로그의 날짜

//      0. Query 설정
//      분류하고자 하는 url 을 통해 우선적인 데이터 추출 하고자 함
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery("current_path", url))
				.filter(QueryBuilders.rangeQuery("currentDate").lte(endDate))
				.filter(QueryBuilders.rangeQuery("currentDate").gte(startDate));

//      1. SearchSourceBuilder 설정
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(queryBuilder);
		sourceBuilder.aggregation(termsAgg1);
		sourceBuilder.aggregation(termsAgg2);
		sourceBuilder.aggregation(termsAgg3);

//      2. SearchRequest 설정
//      미리 설정해둔 1차 데이터 검색을 위한 쿼리 지정과 이를 분류할 기준에 대해서 설정해 줌
		SearchRequest searchRequest = new SearchRequest(idxName);
		searchRequest.source(sourceBuilder);

//      3. SearchResponse 설정
//      Dev Tools 에서 쿼리 작성 후 얻는 결과물과 동일한 결과를 SearchResponse 를 통해 얻게 됨
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		Terms aggOs = response.getAggregations().get("by_os");
		Terms aggUserAgent = response.getAggregations().get("by_useragent");
		Terms aggDate = response.getAggregations().get("by_date");

//      4. 결과를 HashMap 에 담아서 전달
		Map<String, Map<String, Long>> result = new HashMap<>();

		Map<String, Long> osList = new HashMap<>();
		Map<String, Long> snsList = new HashMap<>();
		Map<String, Long> browserList = new HashMap<>();
		Map<String, Long> dayOfWeek = new HashMap<>();

		// 사용자 기기 집계 데이터
		for (Terms.Bucket entry : aggOs.getBuckets()) {
			osList.put(entry.getKey().toString(), entry.getDocCount());
		}
		result.put("OS", osList);

		// Browser, SNS 집계 데이터
		for (Terms.Bucket entry : aggUserAgent.getBuckets()) {
			browserList.put(entry.getKey().toString(), entry.getDocCount());
		}

		String[] mySns = {"Facebook", "Instagram", "Twitter", "Kakaotalk"};
		for (String sns : mySns) {
			if (browserList.containsKey(sns)) {
				snsList.put(sns, browserList.get(sns));
				browserList.remove(sns);
			}
		}
		result.put("Browser", browserList);
		result.put("Sns", snsList);

		// 요일 집계
		for (Terms.Bucket entry : aggDate.getBuckets()) {
			LocalDateTime temp = LocalDateTime.ofInstant(Instant.ofEpochMilli((Long) entry.getKey()), TimeZone.getDefault().toZoneId());
			DayOfWeek tempDayOfWeek = temp.getDayOfWeek();

			dayOfWeek.put(tempDayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US), entry.getDocCount());
		}
		result.put("DayOfWeek", dayOfWeek);

		return result;
	}
}