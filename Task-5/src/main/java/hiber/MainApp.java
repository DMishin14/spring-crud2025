package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.List;

public class MainApp {
   public static void main(String[] args) throws SQLException {

      // 1️⃣ Создание Spring-контекста из класса конфигурации
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(AppConfig.class);

      // 2️⃣ Получение сервиса UserService
      UserService userService = context.getBean(UserService.class);

      // 3️⃣ Создание пользователей и машин, связывание их друг с другом
      Car car1 = new Car("Toyota", 123);
      User user1 = new User("User1", "Lastname1", "user1@mail.ru");
      user1.setCar(car1);   // связываем машину с пользователем
      car1.setUser(user1);  // двусторонняя связь

      Car car2 = new Car("BMW", 456);
      User user2 = new User("User2", "Lastname2", "user2@mail.ru");
      user2.setCar(car2);
      car2.setUser(user2);

      // 4️⃣ Добавление пользователей (и машин автоматически) в базу
      userService.add(user1);
      userService.add(user2);

      // 5️⃣ Вывод всех пользователей с их машинами
      List<User> users = userService.listUsers();
      for (User user : users) {
         System.out.println("Id = " + user.getId());
         System.out.println("First Name = " + user.getFirstName());
         System.out.println("Last Name = " + user.getLastName());
         System.out.println("Email = " + user.getEmail());
         if (user.getCar() != null) {
            System.out.println("Car model = " + user.getCar().getModel());
            System.out.println("Car series = " + user.getCar().getSeries());
         }
         System.out.println();
      }

      // 6️⃣ Пример поиска пользователя по машине через HQL
      User owner = userService.getUserByCar("BMW", 456);
      System.out.println("Владелец машины BMW 456: " + owner.getFirstName() + " " + owner.getLastName());

      // 7️⃣ Закрытие контекста
      context.close();
   }
}
