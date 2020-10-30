package io.github.leases.internal.report;

public interface Report<Report, Parameter> {

    Report createReport(Parameter reportParameter);
}
