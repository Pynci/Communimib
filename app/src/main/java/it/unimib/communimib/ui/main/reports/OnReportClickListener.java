package it.unimib.communimib.ui.main.reports;

import it.unimib.communimib.model.Report;
import it.unimib.communimib.model.User;

public interface OnReportClickListener {
    void onItemClick(Report report);
    void onCloseReportClick(Report report);
    void onProfileClick(User reportAuthor);
}
