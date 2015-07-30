package org.spo.test.utiltests;

import org.junit.Test;
import org.spo.fw.log.Logger1;
import org.spo.fw.utils.Utils_PageDiff;



public class TestUtil_PageDifference { 
Logger1 log = new Logger1("TestUtil_PageDifference") ;
	@Test
	public void validateCompare() throws Exception{
		try{
			Utils_PageDiff  diff = new Utils_PageDiff();
			String theMore = "Thre was once upon a time a quick brown sicko fox went to a party one day and ruined it all. " +
					"But  that was not the full story, the next day he repented it and said sorry ";
			String theLesser= "quick brown nice nice fox went to party one day and ";
			String result = "";
			result = diff.compareTexts(theLesser, theMore);
			System.err.println("Result:" +result);
					
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void validateCompare2() throws Exception{
		try{
			Utils_PageDiff  diff = new Utils_PageDiff();
			String theMore = "Thre was once upon a time a quick brown sicko fox went to party one day and ruined it all. " +
					"But  that was not the full story, the next day he repented it and said sorry ";
			String theLesser= "quick brown fox sicko went to party one day and ";
			String result = "";
			result = diff.getDiff(theLesser, theMore);
			System.err.println("Result:" +result);
					
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void validateCompare3() throws Exception{
		try{
			Utils_PageDiff  diff = new Utils_PageDiff();
			String theMore = "CareDiscoveryQualityMeasures-EpisodeList||EpisodeListCustomGroupsRe-abstractionEpisodeListAddAdditionalEpisodesRe-abstractionEpisodeListPatientType:AllIPOPRecordStatus:AllCompleteComplete-ErrorsComplete-WarningsIncompleteDischarge/EncounterPeriod:Q1-2015..MR-2015..FE-2015..JA-2015Q4-2014..DE-2014..NO-2014..OC-2014Q3-2014..SE-2014..AU-2014..JL-2014Q2-2014..JU-2014..MA-2014..AP-2014Q1-2014..MR-2014..FE-2014..JA-2014Q4-2013..DE-2013..NO-2013..OC-2013Q3-2013..SE-2013..AU-2013..JL-2013Q2-2013..JU-2013..MA-2013..AP-2013Q1-2013..MR-2013..FE-2013..JA-2013Q4-2012..DE-2012..NO-2012..OC-2012Q3-2012..SE-2012..AU-2012..JL-2012Q2-2012..JU-2012..MA-2012..AP-2012Q1-2012..MR-2012..FE-2012..JA-2012Q4-2011..DE-2011..NO-2011..OC-2011Q3-2011..SE-2011..AU-2011..JL-2011Q2-2011..JU-2011..MA-2011..AP-2011Q1-2011..MR-2011..FE-2011..JA-2011Q4-2010..DE-2010..NO-2010..OC-2010PopulationStatus:SampledInNoMatchingMeasurePopulation:AllPopulationsAMIASCCACGlobal(EDIMMSUBTOB)HBIPSHFOPOPWebPC(baby)PC(mom)PNSCIPSTKVTESub-population:(optional)AllPopulationsIndividualPopulationStatus:AllCompleteIncompleteCMSCertificationNumber:AllCCNs123456555555666666777777Filter:EOCIDPatientID/MRNRoomNumberDateofBirthPatientFirstNamePatientLastNameAlternateFacilityID:PatientType:AllRecordStatus:AllDischarge/EncounterPeriod:Q1-2015PopulationStatus:SampledInPopulation:AllPopulationsSub-population:AllPopulationsIndividualPopulationStatus:AllCMSCertificationNumber:AllCCNsFilterType:EOCIDFilter:AlternateFacilityID:27episodesfoundEpisodeList.Viewallerrors|Viewallcategorizations1PatientID/MRNPatientNameEOCIDDischargeDate/OPEncounterDateRoomNumberDateofBirthMeasurePopulationOriginalRecordStatusRecordStatusOriginalAbstractorRe-abstractorAlternateFacilityIDFollowUp1123456last_name_1first_name_1DRAFTOneofeachOPCP1Q20151/15/20157/19/1973OPCP(C)CompleteCompleteABSTRACT01ABSTRACT012123456last_name_1first_name_1DRAFTOneofeachOPED1Q20151/15/20157/19/1973OPED(C)CompleteCompleteABSTRACT01ABSTRACT013123456last_name_1first_name_1DRAFTOneofeachOPPainMgmt1Q20151/15/20157/19/1973OPPM(C)CompleteCompleteABSTRACT01ABSTRACT014123456last_name_1first_name_1DRAFTOneofeachOPSTK1Q20151/15/20157/19/1973OPED(C)OPSTK(C)CompleteCompleteABSTRACT01ABSTRACT015123456last_name_1first_name_1DRAFTOneofeachASC-91Q20151/17/20157/19/1961ASC-9(C)OPWeb-29(C)CompleteCompleteABSTRACT01ABSTRACT016123456last_name_1first_name_1DRAFTOneofeachOPWEB-291Q20151/17/20157/19/1961ASC-9(C)OPWeb-29(C)CompleteCompleteABSTRACT01ABSTRACT017123456last_name_1first_name_1DRAFTOneofeachCAC1Q20151/22/201511/4/2003CAC(C)CompleteCompleteABSTRACT01ABSTRACT018123456last_name_1first_name_1DRAFTOneofeachPCMother1Q20151/22/20151/17/1993PC(mom)(C)CompleteCompleteABSTRACT01ABSTRACT019123456last_name_1first_name_1DRAFTOneofeachSCIP1Q20151/22/201510/18/1992SCIP-Inf-CABG(C)CompleteCompleteABSTRACT01ABSTRACT0110123456last_name_1first_name_1DRAFTOneofeachOPAMI1Q20152/14/201510/17/1973OPAMI(C)CompleteCompleteABSTRACT01ABSTRACT0111123456last_name_1first_name_1DRAFTOneofeachOPSCIP1Q20152/14/20157/19/1973OPSurgical(C)CompleteComplete-warningsABSTRACT01ABSTRACT0112123456last_name_1first_name_1DRAFTOneofeachASC-101Q20152/19/20157/19/1961ASC-10(C)OPWeb-30(C)CompleteCompleteABSTRACT01ABSTRACT0113123456last_name_1first_name_1DRAFTOneofeachOPWEB-301Q20152/19/20157/19/1961ASC-10(C)OPWeb-30(C)CompleteCompleteABSTRACT01ABSTRACT0114baseAMI_1last_name_1first_name_1DRAFTOneofeachAMI1Q20151/22/201510/18/1993AMI(C)CompleteCompleteABSTRACT01ABSTRACT0115baseHF_23last_name_1first_name_1DRAFTOneofEachED1Q20151/22/201510/18/1993HF(C)CompleteCompleteABSTRACT01ABSTRACT0116baseHF_23last_name_1first_name_1DRAFTOneofEachHF1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)HF(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0117baseHF_23last_name_1first_name_1DRAFTOneofEachSUB1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0118baseHF_23last_name_1first_name_1DRAFTOneofEachTOB1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0119baseHF_23last_name_1first_name_1DRAFTOneofEachVTE1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)HF(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0120baseHF_23last_name_1first_name_1DRAFTOneofEachVTEDpopOther1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)HF(C)VTE-OtherVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0121baseHF_23last_name_1first_name_1DRAFTOneofeachHBIPSNORX1Q20152/15/201510/18/1963HBIPSICD-9age18-64(C)CompleteCompleteABSTRACT01ABSTRACT0122baseHF_23last_name_1first_name_1DRAFTOneofeachHBIPSRX1Q20152/15/201510/18/1963Global(EDIMMSUBTOB)(C)HBIPSICD-9age18-64(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0123basePNcase1last_name_1first_name_1DRAFTOneofeachPN1Q20151/24/20153/20/1994Global(EDIMMSUBTOB)(C)PN(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0" +
					"But  that was not the full story, the next day he repented it and said sorry ";
			String theLesser= "Re-abstractionEpisodeListPatientType:AllIPOPRecordStatus:AllCompleteComplete-ErrorsComplete-WarningsIncompleteDischarge/EncounterPeriod:Q1-2015..MR-2015..FE-2015..JA-2015Q4-2014..DE-2014..NO-2014..OC-2014Q3-2014..SE-2014..AU-2014..JL-2014Q2-2014..JU-2014..MA-2014..AP-2014Q1-2014..MR-2014..FE-2014..JA-2014Q4-2013..DE-2013..NO-2013..OC-2013Q3-2013..SE-2013..AU-2013..JL-2013Q2-2013..JU-2013..MA-2013..AP-2013Q1-2013..MR-2013..FE-2013..JA-2013Q4-2012..DE-2012..NO-2012..OC-2012Q3-2012..SE-2012..AU-2012..JL-2012Q2-2012..JU-2012..MA-2012..AP-2012Q1-2012..MR-2012..FE-2012..JA-2012Q4-2011..DE-2011..NO-2011..OC-2011Q3-2011..SE-2011..AU-2011..JL-2011Q2-2011..JU-2011..MA-2011..AP-2011Q1-2011..MR-2011..FE-2011..JA-2011Q4-2010..DE-2010..NO-2010..OC-2010PopulationStatus:SampledInNoMatchingMeasurePopulation:AllPopulationsASCOPOPWebSub-population:(optional)AllPopulationsIndividualPopulationStatus:AllCompleteIncompleteCMSCertificationNumber:AllCCNs123456555555666666777777Filter:EOCIDPatientID/MRNRoomNumberDateofBirthPatientFirstNamePatientLastNameAlternateFacilityID:PatientType:OPRecordStatus:AllDischarge/EncounterPeriod:Q1-2015PopulationStatus:SampledInPopulation:AllPopulationsSub-population:AllPopulationsIndividualPopulationStatus:AllCMSCertificationNumber:AllCCNsFilterType:EOCIDFilter:AlternateFacilityID:1episodesfoundEpisodeList.Viewallerrors|Viewallcategorizations1PatientID/MRNPatientNameEOCIDDischargeDate/OPEncounterDateRoomNumberDateofBirthMeasurePopulationOriginalRecordStatusRecordStatusOriginalAbstractorRe-abstractorAlternateFacilityIDFollowUp";
			String result = "";
			result = diff.compareTexts(theLesser, theMore);
			System.err.println("Result:"+'\n' +result);
					
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testDiff() throws Exception{
		try{
			Utils_PageDiff  diff = new Utils_PageDiff();
			String theMore = "CareDiscoveryQualityMeasures-EpisodeList||EpisodeListCustomGroupsRe-abstractionEpisodeListAddAdditionalEpisodesRe-abstractionEpisodeListPatientType:AllIPOPRecordStatus:AllCompleteComplete-ErrorsComplete-WarningsIncompleteDischarge/EncounterPeriod:Q1-2015..MR-2015..FE-2015..JA-2015Q4-2014..DE-2014..NO-2014..OC-2014Q3-2014..SE-2014..AU-2014..JL-2014Q2-2014..JU-2014..MA-2014..AP-2014Q1-2014..MR-2014..FE-2014..JA-2014Q4-2013..DE-2013..NO-2013..OC-2013Q3-2013..SE-2013..AU-2013..JL-2013Q2-2013..JU-2013..MA-2013..AP-2013Q1-2013..MR-2013..FE-2013..JA-2013Q4-2012..DE-2012..NO-2012..OC-2012Q3-2012..SE-2012..AU-2012..JL-2012Q2-2012..JU-2012..MA-2012..AP-2012Q1-2012..MR-2012..FE-2012..JA-2012Q4-2011..DE-2011..NO-2011..OC-2011Q3-2011..SE-2011..AU-2011..JL-2011Q2-2011..JU-2011..MA-2011..AP-2011Q1-2011..MR-2011..FE-2011..JA-2011Q4-2010..DE-2010..NO-2010..OC-2010PopulationStatus:SampledInNoMatchingMeasurePopulation:AllPopulationsAMIASCCACGlobal(EDIMMSUBTOB)HBIPSHFOPOPWebPC(baby)PC(mom)PNSCIPSTKVTESub-population:(optional)AllPopulationsIndividualPopulationStatus:AllCompleteIncompleteCMSCertificationNumber:AllCCNs123456555555666666777777Filter:EOCIDPatientID/MRNRoomNumberDateofBirthPatientFirstNamePatientLastNameAlternateFacilityID:PatientType:AllRecordStatus:AllDischarge/EncounterPeriod:Q1-2015PopulationStatus:SampledInPopulation:AllPopulationsSub-population:AllPopulationsIndividualPopulationStatus:AllCMSCertificationNumber:AllCCNsFilterType:EOCIDFilter:AlternateFacilityID:27episodesfoundEpisodeList.Viewallerrors|Viewallcategorizations1PatientID/MRNPatientNameEOCIDDischargeDate/OPEncounterDateRoomNumberDateofBirthMeasurePopulationOriginalRecordStatusRecordStatusOriginalAbstractorRe-abstractorAlternateFacilityIDFollowUp1123456last_name_1first_name_1DRAFTOneofeachOPCP1Q20151/15/20157/19/1973OPCP(C)CompleteCompleteABSTRACT01ABSTRACT012123456last_name_1first_name_1DRAFTOneofeachOPED1Q20151/15/20157/19/1973OPED(C)CompleteCompleteABSTRACT01ABSTRACT013123456last_name_1first_name_1DRAFTOneofeachOPPainMgmt1Q20151/15/20157/19/1973OPPM(C)CompleteCompleteABSTRACT01ABSTRACT014123456last_name_1first_name_1DRAFTOneofeachOPSTK1Q20151/15/20157/19/1973OPED(C)OPSTK(C)CompleteCompleteABSTRACT01ABSTRACT015123456last_name_1first_name_1DRAFTOneofeachASC-91Q20151/17/20157/19/1961ASC-9(C)OPWeb-29(C)CompleteCompleteABSTRACT01ABSTRACT016123456last_name_1first_name_1DRAFTOneofeachOPWEB-291Q20151/17/20157/19/1961ASC-9(C)OPWeb-29(C)CompleteCompleteABSTRACT01ABSTRACT017123456last_name_1first_name_1DRAFTOneofeachCAC1Q20151/22/201511/4/2003CAC(C)CompleteCompleteABSTRACT01ABSTRACT018123456last_name_1first_name_1DRAFTOneofeachPCMother1Q20151/22/20151/17/1993PC(mom)(C)CompleteCompleteABSTRACT01ABSTRACT019123456last_name_1first_name_1DRAFTOneofeachSCIP1Q20151/22/201510/18/1992SCIP-Inf-CABG(C)CompleteCompleteABSTRACT01ABSTRACT0110123456last_name_1first_name_1DRAFTOneofeachOPAMI1Q20152/14/201510/17/1973OPAMI(C)CompleteCompleteABSTRACT01ABSTRACT0111123456last_name_1first_name_1DRAFTOneofeachOPSCIP1Q20152/14/20157/19/1973OPSurgical(C)CompleteComplete-warningsABSTRACT01ABSTRACT0112123456last_name_1first_name_1DRAFTOneofeachASC-101Q20152/19/20157/19/1961ASC-10(C)OPWeb-30(C)CompleteCompleteABSTRACT01ABSTRACT0113123456last_name_1first_name_1DRAFTOneofeachOPWEB-301Q20152/19/20157/19/1961ASC-10(C)OPWeb-30(C)CompleteCompleteABSTRACT01ABSTRACT0114baseAMI_1last_name_1first_name_1DRAFTOneofeachAMI1Q20151/22/201510/18/1993AMI(C)CompleteCompleteABSTRACT01ABSTRACT0115baseHF_23last_name_1first_name_1DRAFTOneofEachED1Q20151/22/201510/18/1993HF(C)CompleteCompleteABSTRACT01ABSTRACT0116baseHF_23last_name_1first_name_1DRAFTOneofEachHF1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)HF(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0117baseHF_23last_name_1first_name_1DRAFTOneofEachSUB1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0118baseHF_23last_name_1first_name_1DRAFTOneofEachTOB1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0119baseHF_23last_name_1first_name_1DRAFTOneofEachVTE1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)HF(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0120baseHF_23last_name_1first_name_1DRAFTOneofEachVTEDpopOther1Q20151/22/201510/18/1993Global(EDIMMSUBTOB)(C)HF(C)VTE-OtherVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0121baseHF_23last_name_1first_name_1DRAFTOneofeachHBIPSNORX1Q20152/15/201510/18/1963HBIPSICD-9age18-64(C)CompleteCompleteABSTRACT01ABSTRACT0122baseHF_23last_name_1first_name_1DRAFTOneofeachHBIPSRX1Q20152/15/201510/18/1963Global(EDIMMSUBTOB)(C)HBIPSICD-9age18-64(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0123basePNcase1last_name_1first_name_1DRAFTOneofeachPN1Q20151/24/20153/20/1994Global(EDIMMSUBTOB)(C)PN(C)VTE-NoVTEDX(C)CompleteCompleteABSTRACT01ABSTRACT0" +
					"But  that was not the full story, the next day he repented it and said sorry ";
			String theLesser= "Re-abstractionEpisodeListPatientType:AllIPOPRecordStatus:AllCompleteComplete-ErrorsComplete-WarningsIncompleteDischarge/EncounterPeriod:Q1-2015..MR-2015..FE-2015..JA-2015Q4-2014..DE-2014..NO-2014..OC-2014Q3-2014..SE-2014..AU-2014..JL-2014Q2-2014..JU-2014..MA-2014..AP-2014Q1-2014..MR-2014..FE-2014..JA-2014Q4-2013..DE-2013..NO-2013..OC-2013Q3-2013..SE-2013..AU-2013..JL-2013Q2-2013..JU-2013..MA-2013..AP-2013Q1-2013..MR-2013..FE-2013..JA-2013Q4-2012..DE-2012..NO-2012..OC-2012Q3-2012..SE-2012..AU-2012..JL-2012Q2-2012..JU-2012..MA-2012..AP-2012Q1-2012..MR-2012..FE-2012..JA-2012Q4-2011..DE-2011..NO-2011..OC-2011Q3-2011..SE-2011..AU-2011..JL-2011Q2-2011..JU-2011..MA-2011..AP-2011Q1-2011..MR-2011..FE-2011..JA-2011Q4-2010..DE-2010..NO-2010..OC-2010PopulationStatus:SampledInNoMatchingMeasurePopulation:AllPopulationsASCOPOPWebSub-population:(optional)AllPopulationsIndividualPopulationStatus:AllCompleteIncompleteCMSCertificationNumber:AllCCNs123456555555666666777777Filter:EOCIDPatientID/MRNRoomNumberDateofBirthPatientFirstNamePatientLastNameAlternateFacilityID:PatientType:OPRecordStatus:AllDischarge/EncounterPeriod:Q1-2015PopulationStatus:SampledInPopulation:AllPopulationsSub-population:AllPopulationsIndividualPopulationStatus:AllCMSCertificationNumber:AllCCNsFilterType:EOCIDFilter:AlternateFacilityID:1episodesfoundEpisodeList.Viewallerrors|Viewallcategorizations1PatientID/MRNPatientNameEOCIDDischargeDate/OPEncounterDateRoomNumberDateofBirthMeasurePopulationOriginalRecordStatusRecordStatusOriginalAbstractorRe-abstractorAlternateFacilityIDFollowUp";
			String result = "";
//			String theMore = "Thre was once upon a time a quick brown sicko fox went to party one day and ruined it all. " +
//					"But  that was not the full story, the next day he repented it and said sorry ";
//			String theLesser= "quick brown nice nice fox went to party one day and ";
			result = diff.getDiff(theLesser, theMore);			
			System.err.println("Result:"+'\n' +result);
					
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}


