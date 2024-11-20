package org.airtribe.employee_tracking_system.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.airtribe.employee_tracking_system.Entity.Employee;
import org.airtribe.employee_tracking_system.Repository.EmployeeRepository;
import org.airtribe.employee_tracking_system.dto.Oauth2UserInfoDto;
import org.airtribe.employee_tracking_system.dto.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

	private final EmployeeRepository _employeeRepository;

	@Override
	@SneakyThrows
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
		System.out.println("Load user " + oAuth2UserRequest);
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
		return processOAuth2User(oAuth2UserRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		String name = (String) oAuth2User.getAttributes().get("given_name");
		String email = (String) oAuth2User.getAttributes().get("email");
		String id = (String) oAuth2User.getAttributes().get("sub");

		if (name == null || email == null || id == null) {
			throw new IllegalArgumentException("Required attributes are missing");
		}

		Oauth2UserInfoDto userInfoDto = Oauth2UserInfoDto
				.builder()
				.name(name)
				.id(id)
				.email(email)
				.build();

		System.out.println("User info is " + userInfoDto);
		Optional<Employee> userOptional = _employeeRepository.findByEmail(userInfoDto.getEmail());
		System.out.println("User is " + userOptional);

		Employee employee = userOptional
				.map(existingUser -> updateExistingUser(existingUser, userInfoDto))
				.orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
		UserPrincipal userPrincipal = UserPrincipal.create(employee, oAuth2User.getAttributes());
		OAuth2User oAuth2User1 = new org.springframework.security.oauth2.core.user.DefaultOAuth2User(userPrincipal.getAuthorities(), oAuth2User.getAttributes(), "email");
		return oAuth2User1;
	}

	private Employee registerNewUser(OAuth2UserRequest oAuth2UserRequest, Oauth2UserInfoDto userInfoDto) {
		Employee employee = new Employee();
		employee.setLastName(oAuth2UserRequest.getClientRegistration().getRegistrationId());
		employee.setFirstName(userInfoDto.getName());  // Here you may split the name if needed
		employee.setEmail(userInfoDto.getEmail());
		return _employeeRepository.save(employee);
	}

	private Employee updateExistingUser(Employee existingEmployee, Oauth2UserInfoDto userInfoDto) {
		existingEmployee.setFirstName(userInfoDto.getName()); // Again, split the name if required
		return _employeeRepository.save(existingEmployee);
	}
}
