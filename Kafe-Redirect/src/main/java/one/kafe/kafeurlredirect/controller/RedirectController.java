package one.kafe.kafeurlredirect.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import one.kafe.kafeurlredirect.service.UrlService;
import one.kafe.kafeurlredirect.type.etc.URLStatus;
import one.kafe.kafeurlredirect.type.vo.SUrlVo;

@Controller
@RequiredArgsConstructor
public class RedirectController {

	private final UrlService urlService;

	@RequestMapping(value = "/{shortUrl}", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView redirect(@PathVariable String shortUrl,
		@RequestParam(name = "pwd", required = false, defaultValue = "") String password,
		@RequestParam(name = "ignore", required = false, defaultValue = "false") Boolean ignore) {

		ModelAndView modelAndView = new ModelAndView();

		Optional<SUrlVo> sUrlVo = urlService.urlRedirectProcess(shortUrl);
		if (sUrlVo.isEmpty()) {
			modelAndView.setViewName("404");
			modelAndView.setStatus(HttpStatus.NOT_FOUND);
		} else {
			SUrlVo url = sUrlVo.get();
			LocalDateTime now = LocalDateTime.now();
			if (!(url.getStartDate().isBefore(now) && url.getEndDate().isAfter(now) && url.isActive())) {
				modelAndView.setViewName("404");
				modelAndView.setStatus(HttpStatus.NOT_FOUND);
			} else if (url.getUrlStatus().equals(URLStatus.NORMAL) || ignore) {
				if (url.hasPassword()) {
					if (BCrypt.verifyer().verify(password.toCharArray(), url.getPassword()).verified) {
						modelAndView.setViewName("redirect:" + url.getOriginalUrl());
						modelAndView.setStatus(HttpStatus.FOUND);
						urlService.increaseAccessCount(url.getSeq());
					} else {
						if (!url.getUrlStatus().equals(URLStatus.NORMAL)) {
							modelAndView.addObject("preview", url.getPreview());
							modelAndView.addObject("originalUrl", url.getOriginalUrl());
							modelAndView.setViewName("warning-password");
						} else {
							modelAndView.setViewName("password");
						}
					}
				} else {
					modelAndView.setViewName("redirect:" + url.getOriginalUrl());
					modelAndView.setStatus(HttpStatus.FOUND);
					urlService.increaseAccessCount(url.getSeq());
				}
			} else {
				if (url.hasPassword()) {
					modelAndView.addObject("originalUrl", url.getOriginalUrl());
					modelAndView.addObject("preview", url.getPreview());
					modelAndView.setViewName("warning-password");
					modelAndView.setStatus(HttpStatus.OK);
				} else {
					modelAndView.addObject("originalUrl", url.getOriginalUrl());
					modelAndView.addObject("preview", url.getPreview());
					modelAndView.setViewName("warning");
					modelAndView.setStatus(HttpStatus.OK);
				}
			}
		}
		return modelAndView;
	}

	@RequestMapping(value = "/api/redirect/health", method = RequestMethod.GET)
	public String healthCheck() {
		return "OK";
	}
}
