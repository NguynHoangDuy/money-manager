package duy.hoang.server.dto;

import lombok.Data;

@Data
public class FilterDTO {
     private String type;
     private String startDate;
     private String endDate;
     private String keyWord;
     private String sortField;
     private String sortDirection;
}    
