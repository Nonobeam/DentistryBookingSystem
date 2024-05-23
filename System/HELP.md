# Guide

### Reference Documentation For Springboot

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.5/maven-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.5/reference/htmlsingle/index.html#web.security)

### Github

#### Clone SWP repository in github to your computer

1. Create a new Folder anywhere you want.
2. Inside folder open a new commandline then use command 
<br> ```git init``` <br>
This comment will init basic environment setup for github inside your folder so that you can work on it with github.
3. Link your folder to the github repo in the Internet 
<br> ```git remote add origin https://github.com/Nonobeam/DentistryBookingSystem.git```
4. Download/Clone the repo to your offline folder <br> ```git clone https://github.com/Nonobeam/DentistryBookingSystem.git```
5. At this moment, you can start working with your code right now but to make sure everything is up-to-date, try to use this command first
<br> ```git fetch``` <br>
After working for a long time, some conflict may come up, try to use git fetch again if you face something like that.


#### Pull request to SWP repository in github to your computer

1. Create your own branch <br> 
```git checkout -b your-branch-name```
2. Make sure you are in your branch<br>
```git status```
<br>This is a very powerful command, if you have time I recommend you to read this<br>
```git status --help```
<br> If the status telling you that you are in the your-branch-name that means you right. If not try to change branch <br>
```git checkout your-branch-name```
3. Now, you can add your changes into the commit message<br>
```git add . ```<br>
```git commit -m "Description of your changes"```
4. Push your changes into your branch<br>
```git push origin your-branch-name```







