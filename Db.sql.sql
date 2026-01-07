-- phpMyAdmin SQL Dump
-- version 5.2.1deb3
-- https://www.phpmyadmin.net/
--
-- Host: devweb2025.cis.strath.ac.uk:3306
-- Generation Time: Dec 01, 2025 at 04:53 PM
-- Server version: 8.0.43-0ubuntu0.24.04.2
-- PHP Version: 8.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `xsb23174`
--




-- --------------------------------------------------------

--
-- Table structure for table `business_rule`
--

CREATE TABLE `business_rule` (
  `rule_id` int NOT NULL,
  `rule_type` enum('max_attempts','compensation_allowed') NOT NULL,
  `rule_value` int NOT NULL,
  `scope` enum('global','course_specific','module_specific') DEFAULT 'global',
  `target_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `business_rule`
--

INSERT INTO `business_rule` (`rule_id`, `rule_type`, `rule_value`, `scope`, `target_id`) VALUES
(1, 'max_attempts', 3, 'global', NULL),
(2, 'compensation_allowed', 2, 'global', NULL),
(3, 'max_attempts', 4, 'course_specific', 1);

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `course_id` int NOT NULL,
  `department_id` int DEFAULT NULL,
  `course_code` varchar(20) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `course_description` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`course_id`, `department_id`, `course_code`, `course_name`, `course_description`) VALUES
(1, 1, 'CS101', 'Computer Science', 'Introduction to Computer Science'),
(2, 2, 'MA202', 'Mathematics', 'Advanced Mathematics'),
(3, 1, 'CS308', 'Software Engineering', 'Software Development Principles');

-- --------------------------------------------------------

--
-- Table structure for table `course_module`
--

CREATE TABLE `course_module` (
  `course_module_id` int NOT NULL,
  `course_id` int NOT NULL,
  `module_id` int NOT NULL,
  `semester` enum('1','2') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `course_module`
--

INSERT INTO `course_module` (`course_module_id`, `course_id`, `module_id`, `semester`) VALUES
(1, 1, 1, '1'),
(2, 1, 2, '1'),
(3, 2, 3, '1'),
(4, 3, 4, '2');


-- --------------------------------------------------------

--
-- Table structure for table `decision`
--

CREATE TABLE `decision` (
  `decision_id` int NOT NULL,
  `student_id` int NOT NULL,
  `decision_type` enum('award','resit','withdraw') NOT NULL,
  `decision_date` date DEFAULT NULL,
  `issued_by` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `decision`
--

INSERT INTO `decision` (`decision_id`, `student_id`, `decision_type`, `decision_date`, `issued_by`) VALUES
(1, 1001, 'award', '2024-12-20', 3001),
(2, 1002, 'resit', '2024-12-20', 3001),
(3, 8, 'award', '2025-11-24', NULL),
(4, 8, 'withdraw', '2025-11-24', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `department_id` int NOT NULL,
  `department_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`department_id`, `department_name`) VALUES
(1, 'Computer Science'),
(2, 'Mathematics'),
(3, 'Business Administration');

-- --------------------------------------------------------

--
-- Table structure for table `enrollment`
--

CREATE TABLE `enrollment` (
  `enrollment_id` int NOT NULL,
  `student_id` int NOT NULL,
  `course_id` int NOT NULL,
  `enrollment_date` date NOT NULL,
  `enrollment_status` enum('active','completed','withdrawn') DEFAULT 'active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `enrollment`
--

INSERT INTO `enrollment` (`enrollment_id`, `student_id`, `course_id`, `enrollment_date`, `enrollment_status`) VALUES
(1, 1001, 1, '2024-09-01', 'active'),
(2, 1002, 2, '2024-09-01', 'active');

-- -------------------------------------------------------

--
-- Table structure for table `lecturer`
--

CREATE TABLE `lecturer` (
  `lecturer_id` int NOT NULL,
  `user_id` int NOT NULL,
  `department_id` int DEFAULT NULL,
  `qualification` enum('PhD','MSc','MBA','BSc','Other') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `lecturer`
--

INSERT INTO `lecturer` (`lecturer_id`, `user_id`, `department_id`, `qualification`) VALUES
(12, 12, NULL, 'Other'),
(2001, 3, 1, 'PhD'),
(2002, 4, 2, 'MSc');

-- --------------------------------------------------------

--
-- Table structure for table `manager`
--

CREATE TABLE `manager` (
  `manager_id` int NOT NULL,
  `user_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `manager`
--

INSERT INTO `manager` (`manager_id`, `user_id`) VALUES
(3001, 5);

-- --------------------------------------------------------

--
-- Table structure for table `module`
--

CREATE TABLE `module` (
  `module_id` int NOT NULL,
  `module_code` varchar(20) NOT NULL,
  `module_name` varchar(100) NOT NULL,
  `credit` int NOT NULL,
  `department_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `module`
--

INSERT INTO `module` (`module_id`, `module_code`, `module_name`, `credit`, `department_id`) VALUES
(1, 'CS101-M1', 'Programming Fundamentals', 10, NULL),
(2, 'CS101-M2', 'Database Systems', 10, NULL),
(3, 'MA202-M1', 'Calculus', 10, NULL),
(4, 'CS308-M1', 'Software Design', 20, NULL),
(5, 'CS-TEST', 'JUST TESTING', 20, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `module_assignment`
--

CREATE TABLE `module_assignment` (
  `assignment_id` int NOT NULL,
  `lecturer_id` int NOT NULL,
  `module_id` int NOT NULL,
  `assignment_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `module_assignment`
--

INSERT INTO `module_assignment` (`assignment_id`, `lecturer_id`, `module_id`, `assignment_date`) VALUES
(1, 2001, 1, '2024-09-01'),
(2, 2001, 2, '2024-09-01'),
(3, 2002, 3, '2024-09-01'),
(4, 2001, 4, '2024-09-01');

-- --------------------------------------------------------

--
-- Table structure for table `module_attempt`
--

CREATE TABLE `module_attempt` (
  `attempt_id` int NOT NULL,
  `student_id` int NOT NULL,
  `module_id` int NOT NULL,
  `attempt_number` int NOT NULL,
  `exam_mark` int DEFAULT NULL,
  `lab_mark` int DEFAULT NULL,
  `overall_mark` int DEFAULT NULL,
  `status` enum('passed','failed') DEFAULT NULL,
  `attempt_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `module_attempt`
--

INSERT INTO `module_attempt` (`attempt_id`, `student_id`, `module_id`, `attempt_number`, `exam_mark`, `lab_mark`, `overall_mark`, `status`, `attempt_date`) VALUES
(1, 1001, 1, 1, 75, 70, 72, 'passed', '2025-11-26'),
(2, 1001, 2, 1, 45, 60, 52, 'passed', '2024-12-18'),
(3, 1002, 3, 1, 52, 55, 42, 'failed', '2025-11-26'),
(4, 1002, 3, 2, 65, 70, 68, 'passed', '2025-01-15');

-- --------------------------------------------------------


--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `student_id` int NOT NULL,
  `user_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`student_id`, `user_id`) VALUES
(1001, 1),
(1002, 2),
(8, 8),
(15, 15);

-- --------------------------------------------------------

--
-- Table structure for table `teaching_material`
--

CREATE TABLE `teaching_material` (
  `material_id` int NOT NULL,
  `module_id` int NOT NULL,
  `lecturer_id` int NOT NULL,
  `material_type` enum('lecture_note','lab_note') NOT NULL,
  `week_number` int NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `upload_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `teaching_material`
--

INSERT INTO `teaching_material` (`material_id`, `module_id`, `lecturer_id`, `material_type`, `week_number`, `file_name`, `upload_date`) VALUES
(1, 1, 2001, 'lecture_note', 1, 'java_introduction.pdf', '2024-09-05'),
(2, 1, 2001, 'lab_note', 1, 'java_setup_lab.pdf', '2024-09-07'),
(3, 1, 2001, 'lecture_note', 2, 'oop_principles.pdf', '2024-09-12'),
(4, 1, 2001, 'lab_note', 2, 'inheritance_lab.pdf', '2024-09-14'),
(5, 2, 2001, 'lecture_note', 1, 'sql_basics.pdf', '2024-09-08'),
(6, 2, 2001, 'lab_note', 1, 'create_tables_lab.pdf', '2024-09-10'),
(7, 2, 2001, 'lecture_note', 2, 'database_design.pdf', '2024-09-15'),
(8, 3, 2002, 'lecture_note', 1, 'limits_continuity.pdf', '2024-09-06'),
(9, 3, 2002, 'lecture_note', 2, 'derivatives.pdf', '2024-09-13'),
(10, 4, 2001, 'lecture_note', 1, 'design_patterns.pdf', '2024-09-10'),
(11, 1, 2001, 'lecture_note', 1, 'Honeywell.pdf', '2025-11-05');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `gender` enum('male','female','other') NOT NULL,
  `dob` date NOT NULL,
  `user_type` enum('student','lecturer','manager') NOT NULL,
  `account_status` enum('pending','active','inactive') DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `email`, `password`, `first_name`, `surname`, `gender`, `dob`, `user_type`, `account_status`) VALUES
(1, 'student1@uni.com', 'test123', 'John', 'Smith', 'male', '2000-05-15', 'student', 'active'),
(2, 'student2@uni.com', 'hashed_password2', 'Sarah', 'Johnson', 'female', '2001-03-20', 'student', 'active'),
(3, 'lecturer1@uni.com', 'hashed_password3', 'Dr. Michael', 'Brown', 'male', '1980-08-10', 'lecturer', 'active'),
(4, 'lecturer2@uni.com', 'hashed_password4', 'Prof. Emily', 'Davis', 'female', '1975-12-05', 'lecturer', 'active'),
(5, 'manager1@uni.com', 'hashed_password5', 'Mr. Robert', 'Wilson', 'male', '1970-02-28', 'manager', 'active'),
(8, 'haroon@gmail.com', 'test', 'haroon', 'wahid', 'male', '2005-03-25', 'student', 'active'),
(12, 'hello@gmail.com', 'test123', 'hiii', 'yoods', 'male', '2005-01-10', 'lecturer', 'active'),
(15, 'valid@test.com', 'pass', 'Test', 'User', 'male', '2025-11-16', 'student', 'pending');

--
-- Indexes for dumped tables
--



--
-- Indexes for table `business_rule`
--
ALTER TABLE `business_rule`
  ADD PRIMARY KEY (`rule_id`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`course_id`),
  ADD KEY `department_id` (`department_id`);

--
-- Indexes for table `course_module`
--
ALTER TABLE `course_module`
  ADD PRIMARY KEY (`course_module_id`),
  ADD KEY `course_id` (`course_id`),
  ADD KEY `module_id` (`module_id`);



--
-- Indexes for table `decision`
--
ALTER TABLE `decision`
  ADD PRIMARY KEY (`decision_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `issued_by` (`issued_by`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`department_id`);

--
-- Indexes for table `enrollment`
--
ALTER TABLE `enrollment`
  ADD PRIMARY KEY (`enrollment_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `course_id` (`course_id`);



--
-- Indexes for table `lecturer`
--
ALTER TABLE `lecturer`
  ADD PRIMARY KEY (`lecturer_id`),
  ADD UNIQUE KEY `user_id` (`user_id`),
  ADD KEY `department_id` (`department_id`);

--
-- Indexes for table `manager`
--
ALTER TABLE `manager`
  ADD PRIMARY KEY (`manager_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `module`
--
ALTER TABLE `module`
  ADD PRIMARY KEY (`module_id`),
  ADD KEY `department_id` (`department_id`);

--
-- Indexes for table `module_assignment`
--
ALTER TABLE `module_assignment`
  ADD PRIMARY KEY (`assignment_id`),
  ADD KEY `lecturer_id` (`lecturer_id`),
  ADD KEY `module_id` (`module_id`);

--
-- Indexes for table `module_attempt`
--
ALTER TABLE `module_attempt`
  ADD PRIMARY KEY (`attempt_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `module_id` (`module_id`);




--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`student_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `teaching_material`
--
ALTER TABLE `teaching_material`
  ADD PRIMARY KEY (`material_id`),
  ADD KEY `module_id` (`module_id`),
  ADD KEY `lecturer_id` (`lecturer_id`);


--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--



--
-- AUTO_INCREMENT for table `business_rule`
--
ALTER TABLE `business_rule`
  MODIFY `rule_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `course`
--
ALTER TABLE `course`
  MODIFY `course_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `course_module`
--
ALTER TABLE `course_module`
  MODIFY `course_module_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `decision`
--
ALTER TABLE `decision`
  MODIFY `decision_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `department_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `enrollment`
--
ALTER TABLE `enrollment`
  MODIFY `enrollment_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `module`
--
ALTER TABLE `module`
  MODIFY `module_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `module_assignment`
--
ALTER TABLE `module_assignment`
  MODIFY `assignment_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `module_attempt`
--
ALTER TABLE `module_attempt`
  MODIFY `attempt_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;



--
-- AUTO_INCREMENT for table `teaching_material`
--
ALTER TABLE `teaching_material`
  MODIFY `material_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;


--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `course`
--
ALTER TABLE `course`
  ADD CONSTRAINT `course_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`);

--
-- Constraints for table `course_module`
--
ALTER TABLE `course_module`
  ADD CONSTRAINT `course_module_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`),
  ADD CONSTRAINT `course_module_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `module` (`module_id`);

--
-- Constraints for table `decision`
--
ALTER TABLE `decision`
  ADD CONSTRAINT `decision_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  ADD CONSTRAINT `decision_ibfk_2` FOREIGN KEY (`issued_by`) REFERENCES `manager` (`manager_id`);

--
-- Constraints for table `enrollment`
--
ALTER TABLE `enrollment`
  ADD CONSTRAINT `enrollment_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  ADD CONSTRAINT `enrollment_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`);

--
-- Constraints for table `lecturer`
--
ALTER TABLE `lecturer`
  ADD CONSTRAINT `lecturer_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `lecturer_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`);

--
-- Constraints for table `manager`
--
ALTER TABLE `manager`
  ADD CONSTRAINT `manager_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `module`
--
ALTER TABLE `module`
  ADD CONSTRAINT `module_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`);

--
-- Constraints for table `module_assignment`
--
ALTER TABLE `module_assignment`
  ADD CONSTRAINT `module_assignment_ibfk_1` FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer` (`lecturer_id`),
  ADD CONSTRAINT `module_assignment_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `module` (`module_id`);

--
-- Constraints for table `module_attempt`
--
ALTER TABLE `module_attempt`
  ADD CONSTRAINT `module_attempt_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`),
  ADD CONSTRAINT `module_attempt_ibfk_2` FOREIGN KEY (`module_id`) REFERENCES `module` (`module_id`);


--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `teaching_material`
--
ALTER TABLE `teaching_material`
  ADD CONSTRAINT `teaching_material_ibfk_1` FOREIGN KEY (`module_id`) REFERENCES `module` (`module_id`),
  ADD CONSTRAINT `teaching_material_ibfk_2` FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer` (`lecturer_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
