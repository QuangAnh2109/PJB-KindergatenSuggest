package fa.appcode.services.impl;

import fa.appcode.common.utils.SchoolConstant;
import fa.appcode.entities.MasterDatum;
import fa.appcode.repositories.MasterDatumRepository;
import fa.appcode.services.MasterDatumService;
import fa.appcode.common.vo.MasterDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MasterDatumServiceImpl implements MasterDatumService {
    @Autowired
    private MasterDatumRepository masterDatumRepository;

    @Override
    public String findNameById(int id) {
        return masterDatumRepository.getById(id).getTypeValue();
    }

    @Override
    public List<MasterDataVo> findAllByTypeNameNoDelete(String typeName) {
        return masterDatumRepository.findAllMasterDataVoByTypeNameAndDeleteFlg(typeName, false);
    }

    @Override
    public List<MasterDataVo> findAllByTypeNameInNoDelete(List<String> typeName) {
        return masterDatumRepository.findAllMasterDataVoByTypeNameInAndDeleteFlg(typeName, false);
    }

  @Override
    public String getMasterByTypeNameAndTypeKey(String typeName, Integer typeKey) {
       return masterDatumRepository.getMasterByTypeNameAndTypeKey(typeName, typeKey);
    }

    @Override
    public List<MasterDatum> getListByTypeName(String typeName) {
        return masterDatumRepository.getMasterByTypeName(typeName);
    }

    @Override
    public Integer getMasterKeyByTypeNameAndTypeValue(String typeName, String typeValue) {
        return masterDatumRepository.getMasterKeyByTypeNameAndTypeValue(typeName,typeValue);
    }

    @Override
    public void setMasterDataToModel(Model model) {
        // Get all master data by type name(SCHOOL TYPE, CHILD RECEIVING AGE, EDUCATION METHOD, FACILITIES, UTILITIES) in no delete
        List<MasterDataVo> masterDataVoList = masterDatumRepository.findAllMasterDataVoByTypeNameInAndDeleteFlg(List.of(SchoolConstant.SCHOOL_TYPE, SchoolConstant.CHILD_RECEIVING_AGE, SchoolConstant.EDUCATION_METHOD, SchoolConstant.FACILITIES, SchoolConstant.UTILITIES), false);

        // Create list to store each type of master data
        List<MasterDataVo> schoolTypes = new ArrayList<>(), childReceivingAges = new ArrayList<>(), educationMethods = new ArrayList<>(), facilities = new ArrayList<>(), utilities = new ArrayList<>();
        for (MasterDataVo vo : masterDataVoList) {
            switch (vo.getTypeName()) {
                case SchoolConstant.SCHOOL_TYPE:
                    schoolTypes.add(vo);
                    break;
                case SchoolConstant.CHILD_RECEIVING_AGE:
                    childReceivingAges.add(vo);
                    break;
                case SchoolConstant.EDUCATION_METHOD:
                    educationMethods.add(vo);
                    break;
                case SchoolConstant.FACILITIES:
                    facilities.add(vo);
                    break;
                case SchoolConstant.UTILITIES:
                    utilities.add(vo);
                    break;
            }
        }

        // Add all master data to model
        model.addAttribute("schoolTypes", schoolTypes);
        model.addAttribute("childReceivingAges", childReceivingAges);
        model.addAttribute("educationMethods", educationMethods);
        model.addAttribute("facilities", facilities);
        model.addAttribute("utilities", utilities);
    }
}
