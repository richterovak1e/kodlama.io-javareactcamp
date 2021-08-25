package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.CandidateJobExperienceService;
import com.finalproject.hrmsbackend.core.business.abstracts.CheckService;
import com.finalproject.hrmsbackend.core.utilities.Msg;
import com.finalproject.hrmsbackend.core.utilities.Utils;
import com.finalproject.hrmsbackend.core.utilities.results.*;
import com.finalproject.hrmsbackend.dataAccess.abstracts.CandidateDao;
import com.finalproject.hrmsbackend.dataAccess.abstracts.CandidateJobExperienceDao;
import com.finalproject.hrmsbackend.dataAccess.abstracts.PositionDao;
import com.finalproject.hrmsbackend.entities.concretes.CandidateJobExperience;
import com.finalproject.hrmsbackend.entities.concretes.dtos.CandidateJobExperienceAddDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CandidateJobExperienceManager implements CandidateJobExperienceService {

    private final CandidateDao candidateDao;
    private final CandidateJobExperienceDao candidateJobExpDao;
    private final PositionDao positionDao;
    private final ModelMapper modelMapper;
    private final CheckService check;

    @Override
    public DataResult<List<CandidateJobExperience>> getAll() {
        return new SuccessDataResult<>(candidateJobExpDao.findAll());
    }

    @Override
    public DataResult<List<CandidateJobExperience>> getByQuitYear(Short sortDirection) {
        Sort sort = Utils.getSortByDirection(sortDirection, "quitYear");
        return new SuccessDataResult<>(Msg.SORT_DIRECTION.getCustom("%s (quitYear)"), candidateJobExpDao.findAll(sort));
    }

    @Override
    public Result add(CandidateJobExperienceAddDto candidateJobExperienceAddDto) {
        Map<String, String> errors = new HashMap<>();
        if (check.notExistsById(candidateDao, candidateJobExperienceAddDto.getCandidateId()))
            errors.put("candidateId", Msg.NOT_EXIST.get());
        if (check.notExistsById(positionDao, candidateJobExperienceAddDto.getPositionId()))
            errors.put("positionId", Msg.NOT_EXIST.get());
        if (check.startEndConflict(candidateJobExperienceAddDto.getStartYear(), candidateJobExperienceAddDto.getQuitYear()))
            errors.put("startYear - quitYear", Msg.START_END_YEAR_CONFLICT.get());
        if (!errors.isEmpty()) return new ErrorDataResult<>(Msg.FAILED.get(), errors);

        CandidateJobExperience candJobExp = modelMapper.map(candidateJobExperienceAddDto, CandidateJobExperience.class);

        CandidateJobExperience savedCandJobExp = candidateJobExpDao.save(candJobExp);
        return new SuccessDataResult<>(Msg.SAVED.get(), savedCandJobExp);
    }

    @Override
    public Result deleteById(int candJobExpId) {
        candidateJobExpDao.deleteById(candJobExpId);
        return new SuccessResult(Msg.DELETED.get());
    }

    @Override
    public Result updateWorkPlace(String workPlace, int candJobExpId) {
        if (check.notExistsById(candidateJobExpDao, candJobExpId))
            return new ErrorResult(Msg.NOT_EXIST.get("candJobExpId"));

        CandidateJobExperience candJobExp = candidateJobExpDao.getById(candJobExpId);
        candJobExp.setWorkPlace(workPlace);
        CandidateJobExperience savedCandJobExp = candidateJobExpDao.save(candJobExp);
        return new SuccessDataResult<>(Msg.UPDATED.get(), savedCandJobExp);
    }

    @Override
    public Result updatePosition(short positionId, int candJobExpId) {
        if (check.notExistsById(candidateJobExpDao, candJobExpId))
            return new ErrorResult(Msg.NOT_EXIST.get("candJobExpId"));
        if (check.notExistsById(positionDao, positionId))
            return new ErrorResult(Msg.NOT_EXIST.get("positionId"));

        CandidateJobExperience candJobExp = candidateJobExpDao.getById(candJobExpId);
        candJobExp.setPosition(positionDao.getById(positionId));
        CandidateJobExperience savedCandJobExp = candidateJobExpDao.save(candJobExp);
        return new SuccessDataResult<>(Msg.UPDATED.get(), savedCandJobExp);
    }

    @Override
    public Result updateStartYear(short startYear, int candJobExpId) {
        if (check.notExistsById(candidateJobExpDao, candJobExpId))
            return new ErrorResult(Msg.NOT_EXIST.get("candJobExpId"));

        CandidateJobExperience candJobExp = candidateJobExpDao.getById(candJobExpId);
        if (candJobExp.getStartYear() == startYear)
            return new ErrorResult(Msg.THE_SAME.get("Start year is"));
        if (check.startEndConflict(startYear, candJobExp.getQuitYear()))
            return new ErrorResult(Msg.START_END_YEAR_CONFLICT.get());

        candJobExp.setStartYear(startYear);
        CandidateJobExperience savedCandJobExp = candidateJobExpDao.save(candJobExp);
        return new SuccessDataResult<>(Msg.UPDATED.get(), savedCandJobExp);
    }

    @Override
    public Result updateQuitYear(Short quitYear, int candJobExpId) {
        if (check.notExistsById(candidateJobExpDao, candJobExpId))
            return new ErrorResult(Msg.NOT_EXIST.get("candJobExpId"));

        CandidateJobExperience candJobExp = candidateJobExpDao.getById(candJobExpId);
        if (check.equals(candJobExp.getQuitYear(), quitYear))
            return new ErrorResult(Msg.THE_SAME.get("QuitYear is"));
        if (check.startEndConflict(candJobExp.getStartYear(), quitYear))
            return new ErrorResult(Msg.START_END_YEAR_CONFLICT.get());

        candJobExp.setQuitYear(quitYear);
        CandidateJobExperience savedCandJobExp = candidateJobExpDao.save(candJobExp);
        return new SuccessDataResult<>(Msg.UPDATED.get(), savedCandJobExp);
    }

}