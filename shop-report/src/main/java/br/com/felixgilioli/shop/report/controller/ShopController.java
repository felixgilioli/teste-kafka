package br.com.felixgilioli.shop.report.controller;

import br.com.felixgilioli.shop.report.dto.ShopReportDTO;
import br.com.felixgilioli.shop.report.repository.ReportRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop_report")
public class ShopController {

    private final ReportRepository reportRepository;

    public ShopController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping
    public List<ShopReportDTO> getShop() {
        return reportRepository.findAll()
                .stream()
                .map(ShopReportDTO::convert)
                .collect(Collectors.toList());
    }
}
