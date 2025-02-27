/*
 * NUSMods API
 * NUSMods API contains data used to render <https://nusmods.com>. It includes data on modules offered by NUS and their timetables, as well as information on the locations the classes take place in. You are welcome to use and experiment with the data, which is extracted from official APIs provided by the Registrar's Office.  The API consists of static JSON files scraped daily from the school's APIs. This means it only partially follow REST conventions, and all resources are read only. All successful responses will return JSON, and all endpoints end in `.json`.  The shape of the data returned by these endpoints are designed for NUSMods in mind. If you have any questions or find that you need the data in other shapes for other purposes, feel free to reach out to us:  - **Telegram**: <https://telegram.me/nusmods> - **Mailing list**: <nusmods@googlegroups.com> (for security related issues please email <mods@nusmods.com> instead)  ## Fetching data  Any HTTP client can be used to fetch data from the API. HTTPS is preferred, but the server will also respond to HTTP requests. The server supports HTTP 1.1 as well as HTTP 2 over HTTPS, and supports gzip compression.  The API has no authentication, and is not rate limited. While the server can respond to a large number of requests simultaneously, we request that you be polite with resource usage so as not to disrupt nusmods.com, which relies on the same API server. In general there is no need to fetch data from the API more than once per day, as that is the frequency at which we update the data.  [CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS) headers are enabled on all endpoints, so client-side JavaScript can use also use the API.  ## TypeScript types  Since the NUSMods is written in TypeScript, typings are available in the source for the scraper. These may be easier to read than the documentation generated by Swagger.  - Module types: <https://github.com/nusmodifications/nusmods/blob/master/scrapers/nus-v2/src/types/modules.ts> - Venue types: <https://github.com/nusmodifications/nusmods/blob/master/scrapers/nus-v2/src/types/venues.ts>  ## Data  Below are some notes about the data returned from the API. Feel free to talk to us or create an issue if any of it is not clear.  ### Module data  Module endpoints return information on modules offered by NUS. Most of the module data is self-explanatory, but some of the data are more complex and is explained here.  #### Lessons  Each lesson in a timetable has a lesson type `lessonType` and class number `ClassNo`. Every student must take one of each lesson type offered by the module. For example, this module offers two tutorials and one lecture. That means the student must attend the lecture, and can choose one of the two tutorials to attend.  ```json {   \"timetable\": [     {       \"classNo\": \"1\",       \"lessonType\": \"Lecture\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Tuesday\",       \"startTime\": \"1600\",       \"endTime\": \"1800\",       \"venue\": \"I3-AUD\"     },     {       \"classNo\": \"01\",       \"lessonType\": \"Tutorial\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Wednesday\",       \"startTime\": \"1100\",       \"endTime\": \"1200\",       \"venue\": \"COM1-0207\"     },     {       \"classNo\": \"02\",       \"lessonType\": \"Tutorial\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Friday\",       \"startTime\": \"0900\",       \"endTime\": \"1000\",       \"venue\": \"COM1-0209\"     }   ] } ```  Each lesson has a `classNo` key. There can be multiple lessons of the same type and class number, in which case students must attend both. In this example, students can choose to attend either lecture group 1 on Tuesdays and Wednesdays, or lecture group 2 on Mondays and Wednesdays.  ```json {   \"timetable\": [     {       \"classNo\": \"1\",       \"lessonType\": \"Lecture\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Tuesday\",       \"startTime\": \"1600\",       \"endTime\": \"1800\",       \"venue\": \"I3-AUD\"     },     {       \"classNo\": \"1\",       \"lessonType\": \"Lecture\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Wednesday\",       \"startTime\": \"1400\",       \"endTime\": \"1500\",       \"venue\": \"I3-AUD\"     },     {       \"classNo\": \"2\",       \"lessonType\": \"Lecture\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Monday\",       \"startTime\": \"1000\",       \"endTime\": \"1200\",       \"venue\": \"I3-AUD\"     },     {       \"classNo\": \"2\",       \"lessonType\": \"Lecture\",       \"weeks\": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13],       \"day\": \"Wednesday\",       \"startTime\": \"1500\",       \"endTime\": \"1600\",       \"venue\": \"I3-AUD\"     }   ] } ```  #### Lesson Weeks  The `weeks` key on lessons can return data in one of two forms.  Weeks is usually a sorted array of numbers. In this case it represents the school weeks the lesson occurs on, from 1 to 13.  Some classes have lessons outside of the school timetable. In this case a `WeekRange` object is returned. The object will always contain a `start` and `end` key representing the start and end date of lessons. The example below has classes every week from 17th Jan to 18th April.  ``` json \"weeks\": {   \"start\": \"2019-01-17\",   \"end\": \"2019-04-18\" } ```  Optionally it can also include `weekInterval`, a positive integer describing the number of weeks between each lesson, and `weeks`, an array of positive integers describing the weeks on which the lesson will fall, with week 1 being the starting date. If these are not present you can assume lessons will occur every week.  The following example has lessons on 17th Jan (week 1), 24th Jan (week 2), 7th Feb (week 4) and 21st Feb (week 6).  ``` json \"weeks\": {   \"start\": \"2019-01-17\",   \"end\": \"2019-02-21\",   \"weeks\": [1, 2, 4, 6] } ```  The following example has lessons on 17th Jan (week 1), 31st Jan (week 3), 14th Feb (week 5) and 28th Feb (week 7).  ``` json \"weeks\": {   \"start\": \"2019-01-17\",   \"end\": \"2019-02-28\",   \"weekInterval\": 2 } ```  #### Workload  The `workload` key can return data in one of two forms.  Workload is usually a **5-tuple of numbers**, describing the estimated number of hours per week the student is expected to put in for the module for **lectures, tutorials, laboratory, projects/fieldwork, and preparatory work** respectively. For example, a workload of `[2, 1, 1, 3, 3]` means the student should spend every week  - 2 hours in lectures - 1 hour in tutorials - 1 hour at the lab - 3 hours doing project work - 3 hours preparing for classes  Each module credit represents 2.5 hours of work each week, so the standard 4 MC module represents 10 hours of work each week. Module credit may not be integers.  Note that this is only an estimate, and may be outdated or differ significantly in reality. Some modules also incorrectly lists the **total** workload hours instead of weekly, so very large values may show up.  This value is parsed from a string provided by the school, and occasionally this field will contain unusual values which cannot be parsed. In this case this field will contain the original **string** instead, which should be displayed as-is to the user.  #### Prerequisite, corequisite and preclusions  These three keys determine whether a student can take a module.  **Prerequisites** are requirements you have to meet before you can take a module. These are usually in the form of other modules (see prerequisite tree below for a machine readable format), but can also be things like 'taken A-level H2 math' or '70 MCs and above'.  **Preclusions** refer to modules or requirements that cannot be taken if this module is taken, and vice versa. These are usually modules whose content overlaps significantly with this module, and can usually be used to replace each other to fulfill prerequisites.  **Corequisites** are modules that must be taken together with this module in the same semester. This usually refer to twined modules - modules which have linked syllabuses.  #### Prerequisite Tree  The `prereqTree` key is return on the individual module endpoint (`/modules/{moduleCode`). Not all modules have prerequisites, and some have prerequisites that cannot be properly represented as a tree, in which case this key will not appear.  This describes the prerequisites that need to be fulfilled before this module can be taken. The data structure is recursive and represents a tree.  ```json {   \"and\": [     \"CS1231\",     {       \"or\": [\"CS1010S\", \"CS1010X\"]     }   ] } ```  In the example, this module requires CS1231 and either CS1010S or CS1010X. This can be visualized as  ```            ┌ CS1231 ── all of ─┤            │         ┌ CS1010            └ one of ─┤                      └ CS1010X ```  The module information also contains the inverse of this, that is, modules whose requirements are fulfilled by this module (taking this module will allow you to take these modules in the following semester). The data is found on the `fulfillRequirements` key as an array of module codes.  ### Venue data  Venue data is simply the module timetable restructured to show the lessons happening at each classroom.  The venue list endpoint returns a list of all locations that are used in the semester. Note that this is not a comprehensive list of locations, but rather just a list of venues that appears in module lessons.  The venue information endpoint returns the full class and occupancy information about a venue. The `classes` key contains a list of lessons similar to the `timetable` key in module data, but without a `venue` key and with `moduleCode`.
 *
 * The version of the OpenAPI document: 2.0.0
 * Contact: nusmods@googlegroups.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;


/**
 * ModuleInformation
 */
@JsonPropertyOrder({
    ModuleInformation.JSON_PROPERTY_MODULE_CODE,
    ModuleInformation.JSON_PROPERTY_TITLE,
    ModuleInformation.JSON_PROPERTY_DESCRIPTION,
    ModuleInformation.JSON_PROPERTY_MODULE_CREDIT,
    ModuleInformation.JSON_PROPERTY_DEPARTMENT,
    ModuleInformation.JSON_PROPERTY_FACULTY,
    ModuleInformation.JSON_PROPERTY_WORKLOAD,
    ModuleInformation.JSON_PROPERTY_PREREQUISITE,
    ModuleInformation.JSON_PROPERTY_PRECLUSION,
    ModuleInformation.JSON_PROPERTY_COREQUISITE,
    ModuleInformation.JSON_PROPERTY_SEMESTER_DATA
})
@javax.annotation.processing.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-10-03T22:22:26.802458+08:00[Asia/Singapore]")
public class ModuleInformation {

    public static final String JSON_PROPERTY_MODULE_CODE = "moduleCode";
    public static final String JSON_PROPERTY_TITLE = "title";
    public static final String JSON_PROPERTY_DESCRIPTION = "description";
    public static final String JSON_PROPERTY_MODULE_CREDIT = "moduleCredit";
    public static final String JSON_PROPERTY_DEPARTMENT = "department";
    public static final String JSON_PROPERTY_FACULTY = "faculty";
    public static final String JSON_PROPERTY_WORKLOAD = "workload";
    public static final String JSON_PROPERTY_PREREQUISITE = "prerequisite";
    public static final String JSON_PROPERTY_PRECLUSION = "preclusion";
    public static final String JSON_PROPERTY_COREQUISITE = "corequisite";
    public static final String JSON_PROPERTY_SEMESTER_DATA = "semesterData";
    private String moduleCode;
    private String title;
    private String description;
    private String moduleCredit;
    private String department;
    private String faculty;
    private Workload workload;
    private String prerequisite;
    private String preclusion;
    private String corequisite;
    private List<ModuleInformationSemesterDataInner> semesterData = new ArrayList<>();

    public ModuleInformation() {
    }

    public ModuleInformation moduleCode(String moduleCode) {
        this.moduleCode = moduleCode;
        return this;
    }

    /**
     * Get moduleCode
     *
     * @return moduleCode
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(example = "CS2100", required = true, value = "")
    @JsonProperty(JSON_PROPERTY_MODULE_CODE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public String getModuleCode() {
        return moduleCode;
    }


    @JsonProperty(JSON_PROPERTY_MODULE_CODE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }


    public ModuleInformation title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get title
     *
     * @return title
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(example = "Computer Organisation", required = true, value = "")
    @JsonProperty(JSON_PROPERTY_TITLE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public String getTitle() {
        return title;
    }


    @JsonProperty(JSON_PROPERTY_TITLE)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setTitle(String title) {
        this.title = title;
    }


    public ModuleInformation description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(example = "The objective of this module is to familiarise students with the fundamentals of computing devices. Through this module students will understand the basics of data representation, and how the various parts of a computer work, separately and with each other. This allows students to understand the issues in computing devices, and how these issues affect the implementation of solutions. Topics covered include data representation systems, combinational and sequential circuit design techniques, assembly language, processor execution cycles, pipelining, memory hierarchy and input/output systems.", required = true, value = "")
    @JsonProperty(JSON_PROPERTY_DESCRIPTION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public String getDescription() {
        return description;
    }


    @JsonProperty(JSON_PROPERTY_DESCRIPTION)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setDescription(String description) {
        this.description = description;
    }


    public ModuleInformation moduleCredit(String moduleCredit) {
        this.moduleCredit = moduleCredit;
        return this;
    }

    /**
     * Get moduleCredit
     *
     * @return moduleCredit
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(example = "4", required = true, value = "")
    @JsonProperty(JSON_PROPERTY_MODULE_CREDIT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public String getModuleCredit() {
        return moduleCredit;
    }


    @JsonProperty(JSON_PROPERTY_MODULE_CREDIT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setModuleCredit(String moduleCredit) {
        this.moduleCredit = moduleCredit;
    }


    public ModuleInformation department(String department) {
        this.department = department;
        return this;
    }

    /**
     * Get department
     *
     * @return department
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(example = "Computer Science", required = true, value = "")
    @JsonProperty(JSON_PROPERTY_DEPARTMENT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public String getDepartment() {
        return department;
    }


    @JsonProperty(JSON_PROPERTY_DEPARTMENT)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setDepartment(String department) {
        this.department = department;
    }


    public ModuleInformation faculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    /**
     * Get faculty
     *
     * @return faculty
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(example = "Computing", required = true, value = "")
    @JsonProperty(JSON_PROPERTY_FACULTY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public String getFaculty() {
        return faculty;
    }


    @JsonProperty(JSON_PROPERTY_FACULTY)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }


    public ModuleInformation workload(Workload workload) {
        this.workload = workload;
        return this;
    }

    /**
     * Get workload
     *
     * @return workload
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")
    @JsonProperty(JSON_PROPERTY_WORKLOAD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public Workload getWorkload() {
        return workload;
    }


    @JsonProperty(JSON_PROPERTY_WORKLOAD)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setWorkload(Workload workload) {
        this.workload = workload;
    }


    public ModuleInformation prerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
        return this;
    }

    /**
     * Get prerequisite
     *
     * @return prerequisite
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(example = "CS1010 or its equivalent", value = "")
    @JsonProperty(JSON_PROPERTY_PREREQUISITE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

    public String getPrerequisite() {
        return prerequisite;
    }


    @JsonProperty(JSON_PROPERTY_PREREQUISITE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }


    public ModuleInformation preclusion(String preclusion) {
        this.preclusion = preclusion;
        return this;
    }

    /**
     * Get preclusion
     *
     * @return preclusion
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(example = "CS1104 or Students from department of ECE", value = "")
    @JsonProperty(JSON_PROPERTY_PRECLUSION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

    public String getPreclusion() {
        return preclusion;
    }


    @JsonProperty(JSON_PROPERTY_PRECLUSION)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPreclusion(String preclusion) {
        this.preclusion = preclusion;
    }


    public ModuleInformation corequisite(String corequisite) {
        this.corequisite = corequisite;
        return this;
    }

    /**
     * Get corequisite
     *
     * @return corequisite
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(example = "Students must take CS2101 in the same semester as this module", value = "")
    @JsonProperty(JSON_PROPERTY_COREQUISITE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

    public String getCorequisite() {
        return corequisite;
    }


    @JsonProperty(JSON_PROPERTY_COREQUISITE)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCorequisite(String corequisite) {
        this.corequisite = corequisite;
    }


    public ModuleInformation semesterData(List<ModuleInformationSemesterDataInner> semesterData) {
        this.semesterData = semesterData;
        return this;
    }

    public ModuleInformation addSemesterDataItem(ModuleInformationSemesterDataInner semesterDataItem) {
        this.semesterData.add(semesterDataItem);
        return this;
    }

    /**
     * Get semesterData
     *
     * @return semesterData
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")
    @JsonProperty(JSON_PROPERTY_SEMESTER_DATA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)

    public List<ModuleInformationSemesterDataInner> getSemesterData() {
        return semesterData;
    }


    @JsonProperty(JSON_PROPERTY_SEMESTER_DATA)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setSemesterData(List<ModuleInformationSemesterDataInner> semesterData) {
        this.semesterData = semesterData;
    }


    /**
     * Return true if this ModuleInformation object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModuleInformation moduleInformation = (ModuleInformation) o;
        return Objects.equals(this.moduleCode, moduleInformation.moduleCode) &&
            Objects.equals(this.title, moduleInformation.title) &&
            Objects.equals(this.description, moduleInformation.description) &&
            Objects.equals(this.moduleCredit, moduleInformation.moduleCredit) &&
            Objects.equals(this.department, moduleInformation.department) &&
            Objects.equals(this.faculty, moduleInformation.faculty) &&
            Objects.equals(this.workload, moduleInformation.workload) &&
            Objects.equals(this.prerequisite, moduleInformation.prerequisite) &&
            Objects.equals(this.preclusion, moduleInformation.preclusion) &&
            Objects.equals(this.corequisite, moduleInformation.corequisite) &&
            Objects.equals(this.semesterData, moduleInformation.semesterData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleCode, title, description, moduleCredit, department, faculty, workload, prerequisite,
            preclusion, corequisite, semesterData);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ModuleInformation {\n");
        sb.append("    moduleCode: ").append(toIndentedString(moduleCode)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    moduleCredit: ").append(toIndentedString(moduleCredit)).append("\n");
        sb.append("    department: ").append(toIndentedString(department)).append("\n");
        sb.append("    faculty: ").append(toIndentedString(faculty)).append("\n");
        sb.append("    workload: ").append(toIndentedString(workload)).append("\n");
        sb.append("    prerequisite: ").append(toIndentedString(prerequisite)).append("\n");
        sb.append("    preclusion: ").append(toIndentedString(preclusion)).append("\n");
        sb.append("    corequisite: ").append(toIndentedString(corequisite)).append("\n");
        sb.append("    semesterData: ").append(toIndentedString(semesterData)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

