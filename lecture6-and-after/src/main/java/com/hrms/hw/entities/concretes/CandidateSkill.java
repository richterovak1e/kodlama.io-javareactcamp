package com.hrms.hw.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "candidates_skills",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"candidate_id", "skill_id"})})
public class CandidateSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidates_skills_id_generator")
    @SequenceGenerator(name = "candidates_skills_id_generator", sequenceName = "candidates_skills_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    @JsonIgnoreProperties(value = {"candidateCvs","candidateImages", "candidateJobExperiences",
            "candidateLanguages", "candidateSchools", "candidateSkills"})
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

}
