package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Branch;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportingTest {

    @Mock
    Branch branch;

    @Mock
    Branch childBranch;

    @Mock
    SavingAccount rootBranchAccount;

    @Mock
    SavingAccount firstChildBranchAccount;
    @Mock
    SavingAccount secondChildBranchAccount;
    @Mock
    Client rootBranchAccountClient;
    @Mock
    Client secondChildBranchAccountClient;

    Reporting sut;

    @BeforeEach
    void setUp() {

        sut = new Reporting();

        when(branch.getId()).thenReturn(1);

        when(childBranch.getId()).thenReturn(1);
        when(childBranch.getAccounts()).thenReturn(Arrays.asList(firstChildBranchAccount, secondChildBranchAccount));

        when(rootBranchAccount.getReportString()).thenReturn("       ### acc 0: 100.0");
        when(rootBranchAccount.getAmount()).thenReturn(100.0);
        when(rootBranchAccount.getClient()).thenReturn(rootBranchAccountClient);

        when(firstChildBranchAccount.getReportString()).thenReturn("       ### acc 1: 100.0");
        when(firstChildBranchAccount.getAmount()).thenReturn(100.0);
        when(firstChildBranchAccount.getClient()).thenReturn(secondChildBranchAccountClient);

        when(secondChildBranchAccount.getReportString()).thenReturn("       ### acc 2: 200.0");
        when(secondChildBranchAccount.getAmount()).thenReturn(200.0);
        when(secondChildBranchAccount.getClient()).thenReturn(secondChildBranchAccountClient);

        when(rootBranchAccountClient.getReportString()).thenReturn("  ## (1) Z K");
        when(secondChildBranchAccountClient.getReportString()).thenReturn("    ## (2) Vas Pupking");
    }


    @Test
    public void shouldReturnEmptyReportWhenBranchIsNull() {

        assertEquals("# BRANCH is empty", sut.getReport(null));
    }

    @Test
    public void shouldReturnEmptyReportWhenAccountAndChildBranchIsEmpty() {

        when(branch.getAccounts()).thenReturn(null);
        when(branch.getChildren()).thenReturn(null);

        assertEquals("# BRANCH 1 (0.0)"
                , sut.getReport(branch)
        );
    }

    @Test
    public void shouldReturnEmptyReportWhenHasAccountsAndChildBranchIsEmpty() {

        when(branch.getAccounts()).thenReturn(Collections.singletonList(rootBranchAccount));
        when(branch.getChildren()).thenReturn(null);

        assertEquals("# BRANCH 1 (100.0)" +
                        "  ## (1) Z K" +
                        "       ### acc 0: 100.0"
                , sut.getReport(branch)
        );
    }


    @Test
    public void shouldReturnEmptyReportWhenHasChildBranchAndAccountsIsEmpty() {

        when(branch.getAccounts()).thenReturn(null);
        when(branch.getChildren()).thenReturn(Collections.singletonList(childBranch));

        assertEquals("# BRANCH 1 (300.0)" +
                        "  # BRANCH 1-1 (300.0)" +
                        "    ## (2) Vas Pupking" +
                        "       ### acc 1: 100.0" +
                        "       ### acc 2: 200.0"
                , sut.getReport(branch)
        );
    }

    @Test
    public void shouldGetReport() {

        when(branch.getAccounts()).thenReturn(Collections.singletonList(rootBranchAccount));
        when(branch.getChildren()).thenReturn(Collections.singletonList(childBranch));

        assertEquals("# BRANCH 1 (400.0)" +
                        "  ## (1) Z K" +
                        "       ### acc 0: 100.0" +
                        "  # BRANCH 1-1 (300.0)" +
                        "    ## (2) Vas Pupking" +
                        "       ### acc 1: 100.0" +
                        "       ### acc 2: 200.0"
                , sut.getReport(branch)
        );
    }

}