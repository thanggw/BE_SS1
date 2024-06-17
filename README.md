# GAMETHEORY + STABLEMATCHING SOLVER BACKEND APPLICATION (JAVA)
## Ứng dụng backend phục vụ cho việc giải các bài toán về game theory và stable matching bằng Java sử dụng Springboot MVC và MOEA Framework

## Table of Contents

- [Installation (Cài đặt)](#installation)
- [Usage (Sử dụng)](#usage)
- [API Documentation (Tài liệu API)](#api-documentation)
- [Contributing (Đóng góp)](#contributing)

## Installation (Cài đặt)

### Requirements (Yêu cầu)
1. github 
2. Java JDK 17
3. Maven 3.8.3+

### Steps (Bước thực hiện)
1. Clone the repository (Clone repository này)
```bash 
git clone https://github.com/suyttthideptrai/SS1_2023_StableMatchingSolver_Backend.git
```
2. Build the project (Build project)
```bash
mvn clean package
```

## Usage (Sử dụng)
Run the built .jar file (Chạy file jar sau khi build xong)
```bash
java -jar target/stable-matching-solver-0.0.1-SNAPSHOT.jar
```
hoặc chạy trực tiếp trên IDE (IntelliJ IDEA, Eclipse, Netbeans, ...) bằng cách chạy class `StableMatchingSolverApplication.java`

## API Documentation (Tài liệu API)
App sẽ được chạy ở local host cổng 8080 (http://localhost:8080) và có thể sử dụng các API sau:

### Game Theory
1. POST `/api/game-theory-solver`
2. POST `/api/problem-result-insights/{sessionCode}`

### Stable Matching
1. POST `/api/stable-matching-solver`
2. POST `/api/matching-problem-result-insights/{sessionCode}`

### Tìm hiểu thêm về các API endpoints tại class `src.main.java.com.example.SS2_Backend.controller.HomeController.java`
### 

## Contributing (Đóng góp)

### Thường thì mỗi khóa sẽ làm một tính năng mới hoặc improve/sửa lỗi các tính năng sẵn có.
### Vì vậy các khóa sau sẽ fork project này và tạo branch mới để làm việc thay vì tạo pull request trực tiếp lên branch `main` hiện tại.
### Các bạn có thể tham khảo cách làm như sau:
1. Fork project này
2. Tạo branch mới (`git checkout -b extra-feature`)
3. Commit thay đổi của bạn (`git commit -m 'Add extra feature'`)
4. Push lên branch (`git push origin extra-feature`)

### Mọi vấn đề khó khăn hoặc thắc mắc có thể liên hệ với các thành viên khóa trước, trợ giảng hoặc thầy cô hướng dẫn.

