package one.kafe.kafepreview.etc;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.kafe.kafepreview.type.dto.PreviewTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class Receiver {

	private final PreviewService previewService;

	@RabbitListener(queues = "preview.queue")
	public void consume(PreviewTemplate previewTemplate) {
		try {
			String objectUrl = previewService.generatePreview(previewTemplate.getUrl());
			previewService.updatePreviewUrl(previewTemplate.getSeq(), objectUrl);
		} catch (MalformedURLException ignore) {
			log.error("Preview Generate Failed : {}", previewTemplate.getUrl());
		}
	}
}
