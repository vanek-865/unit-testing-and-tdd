package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Branch;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

public class Reporting {
    /**
     * @return Markdown report for all branches, clients, accounts
     */
    public String getReport(Branch rootBranch) {

        if (rootBranch == null) return "# BRANCH is empty";
        var result = new ArrayList<String>();
        var accountResult = new ArrayList<String>();
        var totalAmount = 0d;

        if (rootBranch.getAccounts() != null || !rootBranch.getAccounts().isEmpty()) {
            totalAmount = accountResult(rootBranch, accountResult);
        }

        if (rootBranch.getChildren() != null || !rootBranch.getChildren().isEmpty()) {

        }
        result.add("# BRANCH 1 (" + totalAmount + ")");
        result.addAll(accountResult);

        return getResult(result);
    }

    private double accountResult(Branch rootBranch, ArrayList<String> accountResult) {
        var x = rootBranch.getAccounts().stream().collect(groupingBy(Account::getClient));
        AtomicReference<Double> totalAmount = new AtomicReference<>(0d);

        x.entrySet().forEach(
                (it)->{
                    accountResult.add(it.getKey().getReportString());
                    it.getValue().forEach((acc) -> {
                        accountResult.add(acc.getReportString());
                        totalAmount.updateAndGet(v -> (v + acc.getAmount()));
                    });
                }
        );

        return totalAmount.get();
    }

    public String getResult(ArrayList<String> result){
        StringBuilder builder = new StringBuilder();

        result.forEach((it)->{
            builder.append(it);
        });

        return builder.toString();
    }

}
