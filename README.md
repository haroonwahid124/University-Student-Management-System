# University-Student-Management-System
A robust, full-stack University Student Management System (USMS) built with Java Swing and MySQL, featuring MVC architecture and role-based access control for Students, Lecturers, and Managers.

👥 The Team:
This was a collaborative group project developed by:
1) Haroon Wahid 
2) Judin Alex
3) Thomas Clarkson
4) Aljo Meledom
5) Leo Ozturk

🚀 Key Technical Features
Advanced State Synchronization: Solved complex GUI "race conditions" using custom flag-based synchronization to protect state variables during data refresh cycles.
Transactional Integrity: Implemented atomic database operations using commit() and rollback() to ensure zero data orphaned during multi-table signups.
Role-Based Access Control (RBAC): Distinct dashboards and permission sets for Students, Lecturers, and Managers.
Clean Architecture: Utilized the DAO (Data Access Object) Pattern and Singleton Pattern for efficient database connection management.

🛠️ Tech Stack

Language: Java (JDK 17+) 
GUI: Java Swing (Custom UI Forms) 
Database: MySQL (JDBC) 
Architecture: MVC (Model-View-Controller)
