<image src='https://github.com/Beach-Combine/.github/blob/main/images/header.png?raw=true' width="800"/>
<br/>

## ✔️ How to Start
### 1) Prerequisites
* Java 11
* IntelliJ IDEA or eclipse
* MySQL Workbrench
* Redis

### 2) Clone
Clone this repo to your local machine using:  
```
git clone https://github.com/Beach-Combine/Backend.git
```
### 3) Setup
- Add `env.properties` in `resources`
  - Fill in the blank space after the equal sign with your own words
```
application.spring.datasource.url=jdbc:mysql://localhost:3308/beachcombine?serverTimezone=Asia/Seoul
application.spring.datasource.username=root
application.spring.datasource.password=
application.jwt.secret=
application.jwt.secret_refresh=
application.spring.cloud.gcp.storage.credentials.location=classpath:beach-combine-3770712535c0.json
application.spring.cloud.gcp.storage.project-id=
application.spring.cloud.gcp.storage.bucket=
application.spring.cloud.gcp.geocodingAPI=
application.spring.datasource.databaseAPI=
```
- Add `beach-combine-3770712535c0.json` in `resources`
  - Private key issued by GCP (If you don't want to use image-related APIs, it's okay to just create a file and leave the file content empty)
- Creating a database called `beachcombine` in MySQL workbrench
- `compileQuerydsl`
- Change the value of the `ddl-auto` variable inside `application.yml` from `none` to `create` only on the first execution.
- Run `BackendApplication`
<br/>

## 📲 Easy Start

For Android User

1. Download apk file ⭐ [here](https://drive.google.com/file/d/1b2KX29Ry0wW-YsW7Ppdn6b2xL5n8T3l2/view?usp=sharing) ⭐. (Available for Android version 12 or higher)
2. You can use it right away by installing it on your smartphone!

- Currently, this service is providing beta service for Busan, Korea.

<br/>

## 💡 How to use

<image src='https://github.com/Beach-Combine/.github/blob/main/images/longImage.png?raw=true' width="800"/>

<br/>

## 📽 Demo Video Link

[![BeachCombine](https://github.com/Beach-Combine/.github/blob/main/images/video.JPG?raw=true)](https://www.youtube.com/watch?v=QcZ9F5scACw)

<br/>

## 🏛 Project Architecture

<image src='https://github.com/Beach-Combine/.github/blob/main/images/architecture.png?raw=true' width="800"/>

<br/>

## ✅ Server Deployment Process (CI/CD using Github Actions)

### Local : Gradle build, Docker build
1. jar build : `gradle build`
2. image creation : `docker build -t yourAccountName/repositoryName ./`
3. push to Docker Hub : `docker push yourAccountName/repositoryName`

(`AccoutName` and `RepositoryName` are from Docker Hub)

### Server : Deploy
1. Pull from Docker Hub : `docker pull yourAccountName/repositoryName`
2. Create image as configured in Docker-compose.yml : `docker tag yourAccountName/repositoryName dockerImageName`
3. Run Docker Compose : `docker-compose up`

(`dockerImageName` should be written as the image name in Docker-compose.yml)

<br />

## 🛠 Tech Stacks

<image src='https://github.com/Beach-Combine/.github/blob/main/images/techStack.png?raw=true' width="800"/>

<br/>

## 📁 ERD

<image src='https://github.com/Beach-Combine/.github/blob/main/images/erd.png?raw=true' width="800"/>

<br/>

## ❗ GIT Strategy

### 1) Git Workflow

### main → develop → feature/Issue#-feature, fix/Issue#-feature, refactor/Issue#-feature

1. Work individually on each branch `local - feature/Issue#-feature`
2. After completing the task, submit a PR to `remote - develop`.
3. After code review, receive approval and merge
4. Every time a merge occurs in `remote - develop`, all team members pull from `remote - develop` to maintain the latest status

### 2) Commit Convention

| Tag name | Description                                                 |
| -------- | ----------------------------------------------------------- |
| feat     | Commits that add a new feature                              |
| fix      | Commits that fix a bug                                      |
| hotfix   | Fix an urgent bug in issue or QA                            |
| build    | Commits that affect build components                        |
| chore    | Miscellaneous commits                                       |
| style    | Commits for code styling or format                          |
| docs     | Commits that affect documentation only                      |
| test     | Commits that add missing tests or correcting existing tests |
| refactor | Commits for code refactoring                                |

<br/>

## 📑 Coding Convention

### 1) Naming Convention

- Variables, functions, and class names should use camelCase.
- For functions, use a verb followed by a noun.
  e.g.) getInfo()
- Column names stored in the DB should use snake_case.
  e.g.) member_id
- URL names should use kebab-case, consisting of lowercase nouns.
- Use hyphens (-) as separators, and avoid using separators when possible.
  e.g.) **[www.example.com/user](http://www.example.com/user)**

### 2) Builder

- To improve readability, builders are required instead of constructors

<br/>

## 👥 Contributors

|                                    [권보민](https://github.com/pingowl)                                    |                                   [추서연](https://github.com/ChooSeoyeon)                                   |
| :---------------------------------------------------------------------------: | :--------------------------------------------------------------------------: |
| <img src="https://avatars.githubusercontent.com/u/101239440?v=4" width=150px> | <img src="https://avatars.githubusercontent.com/u/83302344?v=4" width=150px> |


<br/>

## 📎 Link

- Email : [t01053604256@gmail.com](mailto:t01053604256@gmail.com)
- [Mobile repository](https://github.com/Beach-Combine/Mobile)
- [Backend repository](https://github.com/Beach-Combine/Backend)
- [AI repository](https://github.com/Beach-Combine/AI)
  <br/>
