package com.vamsi.MoneyManagerApp.controller;

import com.vamsi.MoneyManagerApp.entity.ProfileEntity;
import com.vamsi.MoneyManagerApp.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmailController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final EmailService emailService;
    private final ProfileService profileService;

//    @GetMapping("/income-excel")
//    public ResponseEntity<Void> emailIncomeExcel() throws IOException, MessagingException {
//        ProfileEntity profile = profileService.getCurrentProfile();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        excelService.writeIncomesToExcel(baos, incomeService.getCurrentMonthIncomesOfCurrentUser());
//        emailService.sendEmailWithExcelAttachment(profile.getEmail(),
//                "Your Income Excel Report",
//                "Please find attached your income report",
//                baos.toByteArray(),
//                "income.xlsx");
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }


    @GetMapping("/income-excel")
    public ResponseEntity<Void> emailIncomeExcel(String userEmail) throws Exception {
        // Generate Excel file in memory
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        sheet.createRow(0).createCell(0).setCellValue("Monthly Report Data");
        workbook.write(out);
        workbook.close();

        // Send via Brevo API
        emailService.sendEmailWithExcelAttachment(
                userEmail,
                "📊 Your Monthly Income Report",
                "<h3>Hello!</h3><p>Please find your report attached.</p>",
                out.toByteArray(),
                "income_details.xlsx"
        );
        return new ResponseEntity<>(null,HttpStatus.OK);
    }


    @GetMapping("/expense-excel")
    public ResponseEntity<Void> emailExpenseExcel() throws IOException, MessagingException {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeExpensesToExcel(baos, expenseService.getCurrentMonthExpensesOfCurrentUser());
        emailService.sendEmailWithExcelAttachment(
                profile.getEmail(),
                "Your Expense Excel Report",
                "Please find attached your expense report.",
                baos.toByteArray(),
                "expenses.xlsx");
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}

