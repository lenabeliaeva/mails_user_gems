package org.example.mails.person.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatar;
    private String title;
    private String linkedinUrl;
    private CompanyDto company;
    @Setter
    private Map<String, Integer> pastMeetingsCount;
}
