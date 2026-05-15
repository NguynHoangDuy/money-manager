package duy.hoang.server.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import duy.hoang.server.dto.ExpenseDTO;
import duy.hoang.server.entity.ProfileEntity;
import duy.hoang.server.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {
     private final ExpenseService expenseService;
     private final ProfileRepository profileRepository;
     private final EmailService emailService;

     @Scheduled(cron = "* * 20 * * *", zone = "UTC") // Every day at 8 AM
     public void SendDailyIncomeExpenseReminder() {
          log.info("Job Started: SendDailyIncomeExpenseReminder");

          List<ProfileEntity> profiles = profileRepository.findAll();

          for (ProfileEntity profile : profiles) {
               String body = "Hi" + " " + profile.getFullName() + ",\n\n" +
                         "This is a friendly reminder to log your income and expenses for today!\n\n" +
                         "You can log in here: " + "<a href=\"" + "http://localhost:3000"
                         + "\" style=\"color: #007bff; text-decoration: underline;\">Log In</a>\n\n" +
                         "Best regards,\n" +
                         "Mony Manager Team";
               emailService.sendEmail(profile.getEmail(), "Daily Reminder: Add your income and expenses", body);
          }
     }

     @Scheduled(cron = "* * 21 * * *", zone = "UTC") // Every day at 9 AM
     public void SendDailySummary() {
          log.info("Job Started: SendDailySummary");

          List<ProfileEntity> profiles = profileRepository.findAll();

          for (ProfileEntity profile : profiles) {
               List<ExpenseDTO> todayExpenses = expenseService
                         .getExpensesForUserOnDate(LocalDate.now(ZoneId.of("VN/Ho_Chi_Minh")));
               int i = 0;
               if (!todayExpenses.isEmpty()) {
                    StringBuilder bodyBuilder = new StringBuilder();
                    bodyBuilder.append("<table style=\"border-collapse: collapse; width: 100%;\">")
                              .append("<thead>")
                              .append("<tr>")
                              .append("<th style=\"border: 1px solid #ddd; padding: 8px; text-align: left; background-color: #f2f2f2;\">No.</th>")
                              .append("<th style=\"border: 1px solid #ddd; padding: 8px; text-align: left; background-color: #f2f2f2;\">Name</th>")
                              .append("<th style=\"border: 1px solid #ddd; padding: 8px; text-align: left; background-color: #f2f2f2;\">Amount</th>")
                              .append("<th style=\"border: 1px solid #ddd; padding: 8px; text-align: left; background-color: #f2f2f2;\">Category</th>")
                              .append("<th style=\"border: 1px solid #ddd; padding: 8px; text-align: left; background-color: #f2f2f2;\">Date</th>")
                              .append("</tr>")
                              .append("</thead>")
                              .append("<tbody>");

                    for (ExpenseDTO expense : todayExpenses) {
                         bodyBuilder.append("<tr>")
                                   .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(++i)
                                   .append("</td>")
                                   .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">")
                                   .append(expense.getName()).append("</td>")
                                   .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">")
                                   .append(expense.getAmount()).append("</td>")
                                   .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">")
                                   .append(expense.getCategoryId() != null ? expense.getCategoryName() : "No Category")
                                   .append("</td>")
                                   .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">")
                                   .append(expense.getDate())
                                   .append("</td>")
                                   .append("</tr>");
                    }
                    String body = "Hi" + " " + profile.getFullName() + ",\n\n" +
                              "Here is your summary of expenses for today:\n\n" +
                              (todayExpenses.isEmpty() ? "You have no expenses recorded for today."
                                        : bodyBuilder.append("</tbody>").append("</table>").toString())
                              +
                              "\n\nBest regards,\n" +
                              "Mony Manager Team";
                    emailService.sendEmail(profile.getEmail(), "Daily Summary: Your income and expenses for today",
                              body);
               }
          }
     }
}
