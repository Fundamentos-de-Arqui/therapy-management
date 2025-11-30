package com.soulware.therapymanagement.infrastructure.messaging.dto;

import com.soulware.therapymanagement.application.commands.ScheduleEntryCommand;

import java.time.*;
import java.util.List;
import java.util.Map;

public class TherapyPlanDraftedMessage {

    public IdWrapper id;
    public IdWrapper assessmentId;
    public IdWrapper assignedTherapistId;
    public IdWrapper legalResponsibleId;
    public IdWrapper patientId;

    public ScheduleDTO schedule;
    public TherapyPlanInformationDTO therapyPlanInformation;

    public static class IdWrapper {
        public Long value;
    }

    public static class TherapyPlanInformationDTO {
        public String Description;
        public String Goals;
    }

    public static class ScheduleDTO {
        public boolean isEmpty;
        public Map<String, ScheduleEntryDTO> schedule;
    }

    public static class ScheduleEntryDTO {
        public String start;
        public String end;
    }
}


