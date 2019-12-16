LINK: https://github.com/k0psutin/Cyber-Sec-Project

# FLAW 1:
Cross-Site Scripting

Program is using th:utext instead of `th:text` in `done.html` file, which makes program vulnerable to injections. To fix this, you should rename `th:utext` to `th:text`. `th:utext` makes text unescaped, as where `th:text` goes trough sanitation, to prevent injection.

In `done.html`, replace `th:utext` with `th:text`, and text is sanitized.

# FLAW 2:
Broken Access Control

By disabling CSRF, we can make requests from different origin. To prevent this, we should enable CSRF, and attach CSRF token to POST methods, to validate it originates from same site. Token can be included in the form by using `${_csrf.token}` attribute. 

When making forms, by making a hidden input like: `<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>`, we tell the server, the methods are coming from trusted site.

# FLAW 3:
Injection

By using un-sanitized custom queries like: 
```
String query = "SELECT * FROM Signup WHERE Name = '" + name + "'";
List<Signup> signups = entityManager.createNativeQuery(query, Signup.class).getResultList();
```

Program is vulnerable to SQL injections. If user were to create a signup with name `"Joe ' OR 1=1 OR NAME LIKE '%s"`. You could list all signups from the database.

To fix the problem, avoid from using custom queries where you don't sanitize attributes. If you want to use custom queries, make them into repository, where you can insert attributes like: `"SELECT * FROM Signup WHERE Name = :name"`, so it will be sanitized.

# FLAW 4:
Security Misconfiguration

As the application is, anyone is capable of sending POST requests trough the /form path. In order to control who can use form, we should implement a login system for the application, and restrict post methods via `@Secured` annotations on methods. In order to prevent access from everyone, we should remove the following from SecurityConfiguration:

`http.authorizeRequests().anyRequest().permitAll();`


With the use of antMatchers we can allow only Users with the role of `"USER"` to access the form. For example we could insert 

`http.authorizeRequests().antMatchers("/form").hasRole("USER");`

And so in order to use the form, the user has to be logged in, but it still isn't enough. You could still send info via postman or curl, so by enabling global security with annotation (at the SecurityConfigure class)

@EnableGlobalMethodSecurity(securedEnabled = true),

we can make use of `@Secured("ROLE_USER"))` annotations, which prevent firing methods without proper authentication.

*NOTE* Registration is not implemented *NOTE*

# FLAW 5:
Using Components with Known Vulnerabilities

By DEFAULT you should question, wether your components are up to date or not? You should start scanning your project with Maven Dependancy tool. You can access it by inserting 
```
<plugin>
    <groupId>org.owasp</groupId>
    <artifactId>dependency-check-maven</artifactId>
    <version>5.2.4</version>
    <executions>
      <execution>
        <goals>
          <goal>check</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
```
Inside the `pom.xml`, in the plugin section. And when you clean and build your project, the dependency-check plugin will scan 
the project, informing what actions you should do about your project, and what possible security flaws the project has.

Dependancy check will generate a html called `dependancy-check-report.html` at the `/target` folder. There you can see all the vulnerabilities in the project.

You can fix all the vulnerabilities, by installing all the latest versions of your components. You can run 

`mvn versions:update-parent` to get the latest version.

After the update, if we run `mvn dependency-check:check` again, we can see the vulnerabilities have gone.