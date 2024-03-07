package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Branch;
import com.acme.banking.dbo.domain.SavingAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

public class Reporting {
    /**
     * @return Markdown report for all branches, clients, accounts
     */
    public String getReport(Branch rootBranch) {

        return getReport(rootBranch, null);
    }

    public String getReport(Branch rootBranch, String printId) {

        if (rootBranch == null) return "# BRANCH is empty";
        var result = new ArrayList<String>();
        var accountResult = new ArrayList<String>();
        AtomicReference<Double> totalAmount = new AtomicReference<>(0d);
        var resultChildBranch = new ArrayList<String>();

        if (printId == null) {
            printId = String.valueOf(rootBranch.getId());
        } else {
            printId += "-" + String.valueOf(rootBranch.getId());
        }

        if (rootBranch.getAccounts() != null ) {
            accountResult(rootBranch, accountResult);
        }

        if (rootBranch.getChildren() != null ) {
            String finalPrintId = printId;
            rootBranch.getChildren().forEach((b) -> {
                resultChildBranch.add(getReport(b, finalPrintId));
                totalAmount.updateAndGet(v -> (v + branchAmount(b)));
            });
        }
        totalAmount.updateAndGet(v -> (v + branchAmount(rootBranch)));

        if (printId.equals(String.valueOf(rootBranch.getId()))) {
            result.add("# BRANCH " + printId + " (" + totalAmount + ")");
        } else {
            result.add("  # BRANCH " + printId + " (" + totalAmount + ")");
        }

        result.addAll(accountResult);
        result.addAll(resultChildBranch);

        return getResult(result);
    }

    private void accountResult(Branch rootBranch, ArrayList<String> accountResult) {

        var x = rootBranch.getAccounts().stream().collect(groupingBy(Account::getClient));
        x.entrySet().forEach(
                (it) -> {
                    accountResult.add(it.getKey().getReportString());
                    it.getValue().forEach((acc) -> {
                        accountResult.add(acc.getReportString());
                    });
                }
        );
    }

    private double branchAmount(Branch rootBranch) {

        AtomicReference<Double> totalAmount = new AtomicReference<>(0d);

        if (rootBranch.getAccounts() == null) return 0d;

        rootBranch.getAccounts().forEach(
                (it) -> totalAmount.updateAndGet(v -> v + it.getAmount()));

        return totalAmount.get();
    }

    private String getResult(ArrayList<String> result) {
        StringBuilder builder = new StringBuilder();

        result.forEach((it) -> {
            builder.append(it);
        });

        return builder.toString();
    }

}
