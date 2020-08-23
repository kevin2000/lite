package com.lite.core.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MenuDto {
	private String name;
	private String url;
	private int serial;
	/*private ArrayList<String> roles;
	private ArrayList<String> users;*/
	private ArrayList<MenuDto> subMenus;
}
