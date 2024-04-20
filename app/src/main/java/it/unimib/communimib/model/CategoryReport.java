package it.unimib.communimib.model;

import java.util.List;

public class CategoryReport {

    private String categoryName;
    private List<Report> reportList;

    public CategoryReport(String categoryName, List<Report> reportList) {
        this.categoryName = categoryName;
        this.reportList = reportList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }
}
