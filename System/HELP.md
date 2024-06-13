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

### Docker run
1. Docker run mysql imag
```docker run --name mydb -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mypassword -e MYSQL_DATABASE=mydb -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword -d mysql:latest```

### Resolve conflict

# First Problem
You guys may face some issues while merge or checkout branch, below is some typical way to fix that. For more detail, please read git document
1. Untracked <br>
   ```git reset --hard origin/phucdev```
2. Unstaged <br>
   ```git rm --cached {file-direct-link}``` <br>
   After that commit all and push into your branch.
3. Have a check again to find out there are any others error <br>
   ```git status```

# Merging
At this stage, I hope your branch didn't face any problem <br>
1. Download, pull, clone the source code in main branch <br>
   ```git clone https://github.com/Nonobeam/DentistryBookingSystem.git``` <br>
2. Move to your branch <br>
   ```git checkout {your-branch-name}``` <br>
3. Do merge with main <br>
   ```git merge main```  <br>
   At this step there, there may some warning about which file can auto-merge, which files don't so that just choose your fav App to solve the conflict <br>
4. Open your fav Code editor / Ide
5. The conflict code may have format <br>
```
Your branch <<<<<
======== 
>>>>> main
```
6. Choose which line of code to keep and then delete those above
7. Commit all the change to your branch
8. Git push change to your branch





