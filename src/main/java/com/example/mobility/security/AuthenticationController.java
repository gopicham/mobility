/*
 * package com.example.mobility.security;
 * 
 * import lombok.AllArgsConstructor;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.*;
 * 
 * @AllArgsConstructor
 * 
 * @RestController
 * 
 * @RequestMapping("/api/auth") public class AuthenticationController {
 * 
 * @Autowired private AuthService authService;
 * 
 * // Build Login REST API
 * 
 * @PostMapping("/login") public ResponseEntity<JWTAuthResponse>
 * authenticate(@RequestBody LoginDto loginDto) { String token =
 * authService.login(loginDto);
 * 
 * JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
 * jwtAuthResponse.setAccessToken(token);
 * 
 * return ResponseEntity.ok(jwtAuthResponse); } }
 */