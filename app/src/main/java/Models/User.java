package Models;

public class User {
        private int UserId;
        private String FirstName;
        private String LastName;
        private String Email;
        private String Password;
        private int Age;
        private int Height;
        private int Weight;

        public User() {
        }

        public User(String email, String password) {
                Email = email;
                Password = password;
        }

        public User(String firstName, String lastName, String email, String password, int age, int height, int weight) {
                this(email, password);
                FirstName = firstName;
                LastName = lastName;
                Age = age;
                Height = height;
                Weight = weight;
        }

        public User(int userId, String firstName, String lastName, String email, String password, int age, int height, int weight) {
                this(firstName, lastName, email, password, age, height, weight);
                this.UserId = userId;
        }


        public int getUserId() {
                return UserId;
        }

        public void setUserId(int userId) {
                UserId = userId;
        }

        public String getFirstName() {
                return FirstName;
        }

        public void setFirstName(String firstName) {
                FirstName = firstName;
        }

        public String getLastName() {
                return LastName;
        }

        public void setLastName(String lastName) {
                LastName = lastName;
        }

        public String getEmail() {
                return Email;
        }

        public void setEmail(String email) {
                Email = email;
        }

        public String getPassword() {
                return Password;
        }

        public void setPassword(String password) {
                Password = password;
        }

        public int getAge() {
                return Age;
        }

        public void setAge(int age) {
                Age = age;
        }

        public int getHeight() {
                return Height;
        }

        public void setHeight(int height) {
                Height = height;
        }

        public int getWeight() {
                return Weight;
        }

        public void setWeight(int weight) {
                Weight = weight;
        }

        @Override
        public String toString() {
                return "User{" +
                        "UserId=" + UserId +
                        ", FirstName='" + FirstName + '\'' +
                        ", LastName='" + LastName + '\'' +
                        ", Email='" + Email + '\'' +
                        ", Password='" + Password + '\'' +
                        ", Age=" + Age +
                        ", Height=" + Height +
                        ", Weight=" + Weight +
                        '}';
        }

        public String[] getFieldNameString()
        {
               return new String[]{"FirstName", "LastName", "Email", "Password", "Age", "Height","Weight"};
        }
}
