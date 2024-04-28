package it.unimib.communimib.datasource.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.communimib.Callback;
import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;

public class FakeReportRemoteDataSource implements IReportRemoteDataSource {

    public Callback addedCallback;
    public Callback changedCallback;
    public Callback removedCallback;
    public Callback cancelledCallback;
    public Map<String, Report> reports;
    public List<String> buildings;
    public String keyword;
    public String author;

    public FakeReportRemoteDataSource(){
        reports = new HashMap<>();
    }

    @Override
    public void readAllReports(Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void readReportsByBuildings(List<String> buildings, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.buildings = buildings;
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void readReportsByTitleAndDescription(String keyword, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.keyword = keyword;
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void readReportsByUID(String author, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback) {
        this.author = author;
        this.addedCallback = addedCallback;
        this.changedCallback = changedCallback;
        this.removedCallback = removedCallback;
        this.cancelledCallback = cancelledCallback;
    }

    @Override
    public void createReport(Report report, Callback callback) {
        report.setRid("54321");
        if(reports.containsKey(report.getRid())){
            reports.replace(report.getRid(), report);
        }
        else{
            reports.put(report.getRid(), report);
        }
        callback.onComplete(new Result.Success());
    }

    @Override
    public void deleteReport(Report report, Callback callback) {
        if(reports.containsKey(report.getRid())){
            reports.remove(report.getRid(), report);
            callback.onComplete(new Result.Success());
        }
        else{
            callback.onComplete(new Result.Error(ErrorMapper.REPORT_DELETING_ERROR));
        }
    }
}
