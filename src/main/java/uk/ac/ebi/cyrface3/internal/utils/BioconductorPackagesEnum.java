package uk.ac.ebi.cyrface3.internal.utils;

public enum BioconductorPackagesEnum {
	
	ANNAFFY ("annaffy"),
	AFFY ("affy"),
	AFFYIO ("affyio"),
	AFFYPLM ("affyPLM"),
	ANNOTATE ("annotate"),
	ANNOTATIONDBI ("AnnotationDbi"),
	ARRAYEXPRESS ("ArrayExpress"),
	BIOBASE ("Biobase"),
	BIOCONDUCTOR ("BiocInstaller"),
	BIOCGENERICS ("BiocGenerics"),
	BIOMART ("biomaRt"),
	BIOSTRINGS ("Biostrings"),
	CAIRO ("Cairo"),
	CELLNOPTR ("CellNOptR"),
	CUMMERBUND ("cummeRbund"),
	HGU133A2_DB ("hgu133a2.db"),
	HGU133A_DB ("hgu133a.db"),
	HGU133PLUS2_DB ("hgu133plus2.db"),
	IRANGES ("IRanges"),
	GCRMA ("gcrma"),
	GENEFILTER ("genefilter"),
	GENEPLOTTER ("geneplotter"),
	GENOMICRANGES ("GenomicRanges"),
	GEOQUERY ("GEOquery"),
	GRAPH ("graph"),
	LIMMA ("limma"),
	LUMI ("lumi"),
	MARRAY ("marray"),
	MULTTEST ("multtest"),
	PREPROCESSCORE ("preprocessCore"),
	QVALUE ("qvalue"),
	RBGL ("RBGL"),
	RGRAPHVIZ ("Rgraphviz"),
	RTRACKLAYER ("rtracklayer"),
	RSAMTOOLS ("Rsamtools"),
	VSN ("vsn"),
	ZLIBBIOC ("zlibbioc");
	
	
	private String packageName;
	
	private BioconductorPackagesEnum(String packageName){
		this.packageName = packageName; 
	}
	
	public String getPackageName(){
		return packageName;
	}
}
