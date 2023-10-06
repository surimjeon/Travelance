package com.easyone.travelance.domain.travel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoticeRequestDto {

    private Long roomId;
    private String title;
    private String content;
    private String link;

}
