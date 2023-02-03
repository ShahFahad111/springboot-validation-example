package com.javavalidation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javavalidation.service.SniRejectExportService;

@RestController
@RequestMapping("/export")
public class SniRejectExportCsvController {
	
	@Autowired
	private SniRejectExportService sniRejectExportService;

	@PostMapping(value = "/csv")
	public String exportCsv() {
		sniRejectExportService.exportSniReject();
		return "Csv Export";
	}
}
