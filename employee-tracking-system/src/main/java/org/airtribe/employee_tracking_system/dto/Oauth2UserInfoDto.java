package org.airtribe.employee_tracking_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Oauth2UserInfoDto {
	private String name;
	private String id;
	private String email;
}
