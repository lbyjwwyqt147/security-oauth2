# spring boot 2.0  security-oauth2

### oauth2 authorization code 大致流程
1. 用户打开客户端以后，客户端要求用户给予授权。
2. 用户同意给予客户端授权。
3. 客户端使用上一步获得的授权，向认证服务器申请令牌。
4. 认证服务器对客户端进行认证以后，确认无误，同意发放令牌。
5. 客户端使用令牌，向资源服务器申请获取资源。
6. 资源服务器确认令牌无误，同意向客户端开放资源。


#### security oauth2 整合的3个核心配置类
1. 资源服务配置  ResourceServerConfiguration   
2. 授权认证服务配置   AuthorizationServerConfiguration
3. security 配置  SecurityConfiguration

### oauth2 根据使用场景不同，分成了4种模式
1. 授权码模式（authorization code  即先登录获取code,再获取token）
2. 简化模式（implicit 在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash）
3. 密码模式（ password 将用户名,密码传过去,直接获取token）
4. 客户端模式（client credentials 无用户,用户向客户端注册,然后客户端以自己的名义向’服务端’获取资源）

demo 中使用了密码授权模式 和客户端授权模式

### 工程结构目录



### pom.xml


    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-jwt</artifactId>
            <version>1.0.9.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>>0.9.0</version>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.20</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    

### 授权认证服务配置类



@Configuration
<br>@EnableAuthorizationServer  //  注解开启验证服务器 提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static final String CLIEN_ID_ONE = "client_1";  //客户端1 用来标识客户的Id
    private static final String CLIEN_ID_TWO = "client_2";  //客户端2
    private static final String CLIEN_ID_THREE = "client_3";  //客户端3
    private static final String CLIENT_SECRET = "secret";   //secret客户端安全码
    private static final String GRANT_TYPE_PASSWORD = "password";   // 密码模式授权模式
    private static final String AUTHORIZATION_CODE = "authorization_code"; //授权码模式  授权码模式使用到了回调地址，是最为复杂的方式，通常网站中经常出现的微博，qq第三方登录，都会采用这个形式。
    private static final String REFRESH_TOKEN = "refresh_token";  //
    private static final String IMPLICIT = "implicit"; //简化授权模式
    private static final String GRANT_TYPE = "client_credentials";  //客户端模式
    private static final String SCOPE_READ = "read";
    private static final String SCOPE_WRITE = "write";
    private static final String TRUST = "trust";
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1*60*60;          //
    private static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6*60*60;        //
    private static final String RESOURCE_ID = "*";    //指定哪些资源是需要授权验证的
    
    
    @Autowired
    private AuthenticationManager authenticationManager;   //认证方式
    @Resource(name = "userService")
    private UserDetailsService userDetailsService;


    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        String secret = new BCryptPasswordEncoder().encode(CLIENT_SECRET);  // 用 BCrypt 对密码编码
        //配置3个个客户端,一个用于password认证、一个用于client认证、一个用于authorization_code认证
        configurer.inMemory()  // 使用in-memory存储
                .withClient(CLIEN_ID_ONE)    //client_id用来标识客户的Id  客户端1
                .resourceIds(RESOURCE_ID)
                .authorizedGrantTypes(GRANT_TYPE, REFRESH_TOKEN)  //允许授权类型   客户端授权模式
                .scopes(SCOPE_READ,SCOPE_WRITE)  //允许授权范围
                .authorities("oauth2")  //客户端可以使用的权限
                .secret(secret)  //secret客户端安全码
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)   //token 时间秒
                .refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS)//刷新token 时间 秒
                .and()
                .withClient(CLIEN_ID_TWO) //client_id用来标识客户的Id  客户端 2
                .resourceIds(RESOURCE_ID)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, REFRESH_TOKEN)   //允许授权类型  密码授权模式
                .scopes(SCOPE_READ,SCOPE_WRITE) //允许授权范围
                .authorities("oauth2") //客户端可以使用的权限
                .secret(secret)  //secret客户端安全码
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)    //token 时间秒
                .refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS); //刷新token 时间 秒

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager).accessTokenConverter(accessTokenConverter())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)  //支持GET  POST  请求获取token
                .userDetailsService(userDetailsService) //必须注入userDetailsService否则根据refresh_token无法加载用户信息
                .reuseRefreshTokens(true);  //开启刷新token
    }


    /**
     * 认证服务器的安全配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()") //isAuthenticated():排除anonymous   isFullyAuthenticated():排除anonymous以及remember-me
                .allowFormAuthenticationForClients();  //允许表单认证
    }




    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("bcrypt");
        return converter;
    }


    @Bean
    public TokenStore tokenStore() {
        //基于jwt实现令牌（Access Token）
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public ResourceServerTokenServices tokenService() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}    

### 资源服务认证配置

@Configuration
<br>@EnableResourceServer  
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "*";
    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(true).tokenServices(tokenServices);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/users/**").authenticated() //配置users访问控制，必须认证过后才可以访问
                .and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint())  //认证失败的业务处理
                .and()
                .logout()
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler());   //退出成功的业务处理
    }


    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

}


### Security 配置

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .requestMatchers().anyRequest()
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());;
    }



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

### security 登录认证

@Service(value = "userService")<br>
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private SysAccountRepository repository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysAccount user = repository.findByUserAccount(username);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserAccount(), user.getUserPwd(), getAuthority());
    }

    private List getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}

### 请求方式说明
1. /oauth/authorize：授权端点。
2. /oauth/token：获取token。
3. /oauth/confirm_access：用户确认授权提交端点。
4. /oauth/error：授权服务错误信息端点。
5. /oauth/check_token：用于资源服务访问的令牌解析端点。
6. /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
7. /oauth/logout: 退出

### 授权码模式

1. 浏览器直接访问地址：http://localhost:18082/oauth/authorize?response_type=code&client_id=client_3&redirect_uri=http://baidu.com

      client_id：第三方应用在授权服务器注册的 Id

      response_type：固定值　code。

      redirect_uri：授权服务器授权重定向哪儿的 URL。

      scope：权限

      state：随机字符串，可以省略
2. 访问连接如果未登陆会跳转到登陆页面
3. 输入数据库中账号和密码登陆后进行授权认可界面 点击“approve” 同意授权获取code返回：https://www.baidu.com/?code=lqByMd&state=123
    点击“deny” 拒绝授权 返回：https://www.baidu.com/?error=access_denied&error_description=User%20denied%20access&state=123
4. 授权之后会得到一个code  https://www.baidu.com/?code=123456
5. 携带code获取token 

    http://localhost:18082/oauth/token?grant_type=authorization_code&code=123456&client_id=client_3&client_secret=secret&redirect_uri=http://baidu.com
 如果出现登陆框这输入账号：client_3 密码 secret 登陆即可获取token信息
 注意：code　只能用一次，如果失败需要重新申请
 
 返回：
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjE4NjM2LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6ImQ2MjE5NDEyLTkxNDEtNDYyNi1iMjdiLWQ0M2ZhMGFkMTgzMSIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.2hsm_qXloexTLeEb1jtPOF6bIkiNYkBjg_Q2Azs9hxU",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZDYyMTk0MTItOTE0MS00NjI2LWIyN2ItZDQzZmEwYWQxODMxIiwiZXhwIjoxNTI5MjM2NjM2LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6ImNhYWFmZTg3LWFhYzgtNDNkNC1iOTQyLTFkMDg3MDZhNjU3OSIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.Wb45Uv4aAae0AuSMttHs5XT6pJ45gGXVWUJBiWAU5UI",
    "expires_in": 3599,
    "scope": "read write",
    "jti": "d6219412-9141-4626-b27b-d43fa0ad1831"
}

### 客户端模式获取token

client模式，没有用户的概念，不需要传递username和password 参数，直接与认证服务器交互，用配置中的客户端信息去申请accessToken，客户端有自己的client_id,client_secret对应于用户的username,password，而客户端也拥有自己的authorities，当采取client模式认证时，对应的权限也就是客户端自己的authorities。
client模式 貌似不支持刷新token请求

请求方式：http://127.0.0.1:18081/oauth/token?grant_type=client_credentials&client_id=client_1&client_secret=secret

1. grant_type : client_credentials   client模式固定值
2. client_id : client_1 对于我们注册客户端的 client_id  在AuthorizationServerConfiguration配置类中
3. client_secret ： secret 对于我们注册客户端的 secret  在AuthorizationServerConfiguration配置类中

返回：
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjI1NzgwLCJhdXRob3JpdGllcyI6WyJvYXV0aDIiXSwianRpIjoiODQ3MTgwZWMtMzM0OS00NDFiLWFlNGEtNTEwZDE3MTc5ZDY3IiwiY2xpZW50X2lkIjoiY2xpZW50XzEifQ.hVFvyYmmE6emq6G8VP8NxujeAYIiM__0Ivr4pDsbqMY",
    "token_type": "bearer",
    "expires_in": 3599,
    "scope": "read write",
    "jti": "847180ec-3349-441b-ae4a-510d17179d67"
}

### 密码模式获取token

password模式，在认证时需要带上自己的用户名和密码，需要传递username和password 参数 ，以及客户端的client_id,client_secret。此时，accessToken所包含的权限是用户本身的权限，而不是客户端的权限。

请求方式：127.0.0.1:18081/oauth/token?username=qiaorulai&password=123456&grant_type=password&client_id=client_2&client_secret=secret

1. username ： 系统的登录名
2. password ： 系统的登录密码
3. grant_type ： password  密码模式固定值
4. client_id : client_2 对于我们注册客户端的 client_id  在AuthorizationServerConfiguration配置类中
5. client_secret ： secret 对于我们注册客户端的 secret  在AuthorizationServerConfiguration配置类中

返回：
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjE4NjM2LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6ImQ2MjE5NDEyLTkxNDEtNDYyNi1iMjdiLWQ0M2ZhMGFkMTgzMSIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.2hsm_qXloexTLeEb1jtPOF6bIkiNYkBjg_Q2Azs9hxU",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiZDYyMTk0MTItOTE0MS00NjI2LWIyN2ItZDQzZmEwYWQxODMxIiwiZXhwIjoxNTI5MjM2NjM2LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6ImNhYWFmZTg3LWFhYzgtNDNkNC1iOTQyLTFkMDg3MDZhNjU3OSIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.Wb45Uv4aAae0AuSMttHs5XT6pJ45gGXVWUJBiWAU5UI",
    "expires_in": 3599,
    "scope": "read write",
    "jti": "d6219412-9141-4626-b27b-d43fa0ad1831"
}

### 密码模式刷新token

请求：http://localhost:18081/oauth/token?grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiYTVkNGQyZTEtZTVhOS00MDA0LWFhNjctMjJlNzk4NGFjZTIzIiwiZXhwIjoxNTI5MjI4MDUxLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjVkNGVmMmNlLTc3MzEtNGVkYS1iZjFmLTkxZWVjYzk4YjQyOCIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.QH76vP6M3FtZt2ijNR3xWCfGGTG28adJdTrUJPztyk8&client_id=client_2&client_secret=secret


1. grant_type ： password  密码模式固定值
2. client_id : client_2 对于我们注册客户端的 client_id  在AuthorizationServerConfiguration配置类中
3. client_secret ： secret 对于我们注册客户端的 secret  在AuthorizationServerConfiguration配置类中
4. refresh_token： 对应签名获取token中的refresh_token值

返回：

{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjE4NzM5LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjAzMjc5NDM5LWNjYjYtNGI3My1iODM4LTgwNTA5YjVkNWFjNiIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.vwXCxlLRKLbqnWm6HuqAVO0j2YzSn1oHQ-GX4LZkEx8",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXRpIjoiMDMyNzk0MzktY2NiNi00YjczLWI4MzgtODA1MDliNWQ1YWM2IiwiZXhwIjoxNTI5MjI4MDUxLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjVkNGVmMmNlLTc3MzEtNGVkYS1iZjFmLTkxZWVjYzk4YjQyOCIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.8ihBYB99dae5MTGrWqXlJGntj4wVr8i6FNCqqAbtwdo",
    "expires_in": 3599,
    "scope": "read write",
    "jti": "03279439-ccb6-4b73-b838-80509b5d5ac6"
}

### 不携带token访问接口
请求：127.0.0.1:18081/users/list
<br>返回：
{
    "timestamp": "2018-06-17T09:26:17.707+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Access Denied",
    "path": "/users/list"
}

### 携带正确的token 访问接口
请求：127.0.0.1:18081/users/list?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjMxMzE3LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6Ijc1Mzg3OWMyLTI4ZDktNDgxMy04YTAxLWZkNzQ4OGNlOWRkMCIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.V9I2lBYKk7sNsygj_bwrJZF06A8LhZx2x_MHmapppGE

返回：
正常返回数据

### 携带不正确的token 访问接口
请求：127.0.0.1:18081/users/list?access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjMxMzE3LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6Ijc1Mzg3OWMyLTI4ZDktNDgxMy04YTAxLWZkNzQ4OGNlOWRkMCIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.V9I2lBYKk7sNsygj_bwrJZF06A8LhZx2x_MHmapppGE

返回：
{
    "error": "invalid_token",
    "error_description": "Access token expired: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiKiJdLCJ1c2VyX25hbWUiOiJxaWFvcnVsYWkiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTI5MjE4NzM5LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjAzMjc5NDM5LWNjYjYtNGI3My1iODM4LTgwNTA5YjVkNWFjNiIsImNsaWVudF9pZCI6ImNsaWVudF8yIn0.vwXCxlLRKLbqnWm6HuqAVO0j2YzSn1oHQ-GX4LZkEx8"
}

参考：http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/