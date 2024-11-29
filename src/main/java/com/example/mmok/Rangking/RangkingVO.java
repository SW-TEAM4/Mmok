package com.example.mmok.Rangking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RangkingVO {

    private String userId;
    private String userName;
    private int winCount;
    private int loseCount;
    private int totalCount;
    private double ranking;

}
