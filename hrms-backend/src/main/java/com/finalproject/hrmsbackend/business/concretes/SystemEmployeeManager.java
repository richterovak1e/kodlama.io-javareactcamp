package com.finalproject.hrmsbackend.business.concretes;

import com.finalproject.hrmsbackend.business.abstracts.SystemEmployeeService;
import com.finalproject.hrmsbackend.core.business.abstracts.CheckService;
import com.finalproject.hrmsbackend.core.utilities.Msg;
import com.finalproject.hrmsbackend.core.utilities.results.*;
import com.finalproject.hrmsbackend.dataAccess.abstracts.SystemEmployeeDao;
import com.finalproject.hrmsbackend.entities.concretes.SystemEmployee;
import com.finalproject.hrmsbackend.entities.concretes.dtos.SystemEmployeesAddDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemEmployeeManager implements SystemEmployeeService {

    private final SystemEmployeeDao systemEmployeeDao;
    private final CheckService check;
    private final ModelMapper modelMapper;

    @Override
    public DataResult<List<SystemEmployee>> getAll() {
        return new SuccessDataResult<>(systemEmployeeDao.findAll());
    }

    @Override
    public DataResult<SystemEmployee> getById(int sysEmplId) {
        return new SuccessDataResult<>(systemEmployeeDao.getById(sysEmplId));
    }

    @Override
    public DataResult<SystemEmployee> getByEmailAndPW(String email, String password) {
        return new SuccessDataResult<>(systemEmployeeDao.getByEmailAndPassword(email, password));
    }

    @Override
    public Result add(SystemEmployeesAddDto systemEmployeesAddDto) {
        SystemEmployee systemEmployee = modelMapper.map(systemEmployeesAddDto, SystemEmployee.class);
        systemEmployeeDao.save(systemEmployee);
        return new SuccessResult(Msg.SAVED.get());
    }

    @Override
    public Result updateFirstName(String firstName, int sysEmplId) {
        if (check.notExistsById(systemEmployeeDao, sysEmplId)) return new ErrorResult(Msg.NOT_EXIST.get("sysEmplId"));

        SystemEmployee sysEmpl = systemEmployeeDao.getById(sysEmplId);
        sysEmpl.setFirstName(firstName);
        return execLastUpdAct(sysEmpl);
    }

    @Override
    public Result updateLastName(String lastName, int sysEmplId) {
        if (check.notExistsById(systemEmployeeDao, sysEmplId)) return new ErrorResult(Msg.NOT_EXIST.get("sysEmplId"));

        SystemEmployee sysEmpl = systemEmployeeDao.getById(sysEmplId);
        sysEmpl.setLastName(lastName);
        return execLastUpdAct(sysEmpl);
    }

    private Result execLastUpdAct(SystemEmployee sysEmpl) {
        sysEmpl.setLastModifiedAt(LocalDateTime.now());
        SystemEmployee savedSysEmpl = systemEmployeeDao.save(sysEmpl);
        return new SuccessDataResult<>(Msg.UPDATED.get(), savedSysEmpl);
    }

}
