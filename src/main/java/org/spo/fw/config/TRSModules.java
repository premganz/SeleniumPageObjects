package org.spo.fw.config;

/**
 * 
 * @author Prem
 *This is a way to externalize a tightly coupled design assumption of the library , namely
 *the need to work with a "page" level validation.
 *If we are denying this core design, we will be killing the spirit of the library, but it makes sense for us to
 *make the text content provider for the 'expected value' of the text representation of each page configurable.
 *
 * This was already dealt with in Issue 25.
 * Now if we choose to work with the TRSConnector, which is via media for the DomainDocumentServer, we have  a case 
 *
 *
 */

@Deprecated
public class TRSModules {
//	enum enumMods {MOD_DOC_RETRIEVE, MOD_DOMAIN_STATE_CHANGE, MOD_XLS_DATA, MOD_DOMAIN_STATE_DATA, MOD_EXTERNAL_PROC_CALL}
//
//public static String mod_doc_page_text="page";
//public static String mod_doc_domain_event="event";
//public static String mod_ext_call="python";
//public static String mod_xls="readXl";
//public static String mod_doc_datareq="datarequest";

	
	
}
