package uk.ac.ebi.cyrface.internal.sbml.sbfc;

import org.sbfc.api.GeneralConverter;
import org.sbfc.api.GeneralModel;

import uk.ac.ebi.cyrface.internal.sbml.simplenet.Network;

public class CellNOpt2Qual extends GeneralConverter
{
	@Override
	public GeneralModel convert(GeneralModel model)
	{
		CellNOptModel inModel = (CellNOptModel)model;
		
		Network net = inModel.getNetwork();

		QualExportHelper sif2qual = new QualExportHelper();
		sif2qual.fromSif(net);
		
		SBMLQualModel outModel = new SBMLQualModel(sif2qual.getSbml());
		return outModel;
	}

	@Override
	public String getResultExtension()
	{
		return ".xml";
	}

	@Override
	public GeneralModel getInputModel()
	{
		return new CellNOptModel();
	}

}
