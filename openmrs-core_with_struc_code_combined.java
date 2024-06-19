File path: openmrs-core/web/src/main/java/org/openmrs/OpenmrsCharacterEscapes.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

/**
 * An instance of this class can be passed to an ObjectMapper instance when serializing objects to
 * JSON using the jackson API so as to escape html and scripts inside html tags
 */
public class OpenmrsCharacterEscapes extends CharacterEscapes {
	
	private final int[] asciiEscapes;
	
	public OpenmrsCharacterEscapes() {
		// start with set of characters known to require escaping (double-quote, backslash etc)
		int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
		
		// and force escaping of a few others:
		esc['<'] = CharacterEscapes.ESCAPE_CUSTOM;
		esc['>'] = CharacterEscapes.ESCAPE_CUSTOM;
		
		asciiEscapes = esc;
	}
	
	@Override
	public int[] getEscapeCodesForAscii() {
		return asciiEscapes;
	}
	
	@Override
	public SerializableString getEscapeSequence(int ch) {
		if (ch == '<') {
			return new SerializedString("&lt;");
		} else if (ch == '>') {
			return new SerializedString("&gt;");
		}
		return null;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/WebUtil.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.GlobalProperty;
import org.openmrs.api.GlobalPropertyListener;
import org.openmrs.api.context.Context;
import org.openmrs.util.Format.FORMAT_TYPE;
import org.openmrs.util.LocaleUtility;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsDateFormat;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil implements GlobalPropertyListener {
	
	private static final Logger log = LoggerFactory.getLogger(WebUtil.class);
	
	/**
	 * Encodes for (X)HTML text content and text attributes.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String escapeHTML(String s) {
		return Encode.forHtml(s);
	}

	/**
	 *  Encodes data for an XML CDATA section.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForCDATA(String s) {
		return Encode.forCDATA(s);
	}

	/**
	 * Encodes for CSS strings.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForCssString(String s) {
		return Encode.forCssString(s);
	}

	/**
	 * Encodes for CSS URL contexts.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForCssUrl(String s) {
		return Encode.forCssUrl(s);
	}

	/**
	 * Encodes for HTML text attributes.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForHtmlAttribute(String s) {
		return Encode.forHtmlAttribute(s);
	}

	/**
	 * Encodes for HTML text content.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForHtmlContent(String s) {
		return Encode.forHtmlContent(s);
	}

	/**
	 * Encodes for unquoted HTML attribute values.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForHtmlUnquotedAttribute(String s) {
		return Encode.forHtmlUnquotedAttribute(s);
	}

	/**
	 * Encodes for a Java string.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForJava(String s) {
		return Encode.forJava(s);
	}

	/**
	 * Encodes for a JavaScript string.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForJavaScript(String s) {
		return Encode.forJavaScript(s);
	}

	/**
	 * This method encodes for JavaScript strings contained within HTML script attributes (such as onclick).
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForJavaScriptAttribute(String s) {
		return Encode.forJavaScriptAttribute(s);
	}

	/**
	 * Encodes for JavaScript strings contained within HTML script blocks.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForJavaScriptBlock(String s) {
		return Encode.forJavaScriptBlock(s);
	}

	/**
	 * Encodes for JavaScript strings contained within a JavaScript or JSON file.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForJavaScriptSource(String s) {
		return Encode.forJavaScriptSource(s);
	}

	/**
	 * Performs percent-encoding of a URL according to RFC 3986.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForUri(String s) {
		return Encode.forUri(s);
	}

	/**
	 * Performs percent-encoding for a component of a URI, such as a query parameter name or value, path or query-string.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForUriComponent(String s) {
		return Encode.forUriComponent(s);
	}

	/**
	 * Encodes for XML and XHTML.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForXml(String s) {
		return Encode.forXml(s);
	}

	/**
	 * Encodes for XML and XHTML attribute content.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForXmlAttribute(String s) {
		return Encode.forXmlAttribute(s);
	}

	/**
	 * Encodes for XML comments.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForXmlComment(String s) {
		return Encode.forXmlComment(s);
	}

	/**
	 * Encodes for XML and XHTML text content.
	 *
	 * @param s
	 * @return Encoded String
	 */
	public static String encodeForXmlContent(String s) {
		return Encode.forXmlContent(s);
	}

	public static String escapeQuotes(String s) {
		String tmpS = s;
		if (tmpS == null) {
			return "";
		}
		
		tmpS = tmpS.replace("\"", "\\\"");
		
		return tmpS;
	}
	
	public static String escapeNewlines(String s) {
		String tmpS = s;
		if (tmpS == null) {
			return "";
		}
		
		tmpS = tmpS.replace("\n", "\\n");
		
		return tmpS;
	}
	
	public static String escapeQuotesAndNewlines(String s) {
		String tmpS = s;
		if (tmpS == null) {
			return "";
		}

		tmpS = tmpS.replace("\"", "\\\"");
		tmpS = tmpS.replace("\r\n", "\\r\\n");
		tmpS = tmpS.replace("\n", "\\n");
		
		return tmpS;
	}
	
	/**
	 * Strips out the path from a string if "C:\documents\file.doc", will return "file.doc" if
	 * "file.doc", will return "file.doc" if "/home/file.doc" will return "file.doc"
	 *
	 * @param filename
	 * @return filename stripped down
	 */
	public static String stripFilename(String filename) {
		log.debug("Stripping filename from: {}", filename);
		
		// for unix based filesystems
		String tmpFilename = filename;
		int index = tmpFilename.lastIndexOf("/");
		if (index != -1) {
			tmpFilename = tmpFilename.substring(index + 1);
		}
		
		// for windows based filesystems
		index = tmpFilename.lastIndexOf("\\");
		if (index != -1) {
			tmpFilename = tmpFilename.substring(index + 1);
		}
		
		log.debug("Returning stripped down filename: {}", tmpFilename);
		
		return tmpFilename;
	}
	
	/**
	 * This method checks if input locale string contains control characters and tries to clean up
	 * actually contained ones. Also it parses locale object from string representation and
	 * validates it object.
	 *
	 * @param localeString input string with locale parameter
	 * @return locale object for input string if CTLs were cleaned up or weren't exist or null if
	 *         could not to clean up CTLs from input string
	 * <strong>Should</strong> ignore leading spaces
	 * <strong>Should</strong> accept language only locales
	 * <strong>Should</strong> not accept invalid locales
	 * <strong>Should</strong> not fail with empty strings
	 * <strong>Should</strong> not fail with whitespace only
	 * <strong>Should</strong> not fail with "_" character only
	 */
	public static Locale normalizeLocale(String localeString) {
		if (localeString == null) {
			return null;
		}
		localeString = localeString.trim();
		if (localeString.isEmpty() || "_".equals(localeString)) {
			return null;
		}
		int len = localeString.length();
		for (int i = 0; i < len; i++) {
			char c = localeString.charAt(i);
			// allow only ASCII letters and "_" character
			if ((c <= 0x20 || c >= 0x7f) || ((c >= 0x20 || c <= 0x7f) && (!Character.isLetter(c) && c != 0x5f))) {
				if (c == 0x09) {
					continue; // allow horizontal tabs
				}
				localeString = localeString.replaceFirst(((Character) c).toString(), "");
				len--;
				i--;
			}
		}
		Locale locale = LocaleUtility.fromSpecification(localeString);
		if (LocaleUtility.isValid(locale)) {
			return locale;
		} else {
			return null;
		}
	}
	
	/**
	 * Convenient method that parses the given string object, that contains locale parameters which
	 * are separated by comma. Tries to clean up CTLs and other unsupported chars within input
	 * string. If invalid locales are included, they are not returned in the resultant list
	 *
	 * @param localesString input string with locale parameters separeted by comma (e.g.
	 *            "en, fr_RW, gh")
	 * @return cleaned up string (or same string) if success or null otherwise
	 * @see #normalizeLocale(String)
	 * <strong>Should</strong> skip over invalid locales
	 * <strong>Should</strong> not fail with empty string
	 */
	public static String sanitizeLocales(String localesString) {
		// quick npe check
		if (localesString == null) {
			return null;
		}
		
		StringBuilder outputString = new StringBuilder();
		
		boolean first = true;
		
		for (String locale : Arrays.asList(localesString.split(","))) {
			Locale loc = normalizeLocale(locale);
			if (loc != null) {
				if (!first) {
					outputString.append(", ");
				} else {
					first = false; // so commas are inserted from now on
				}
				outputString.append(loc.toString());
			}
		}
		if (outputString.length() > 0) {
			return outputString.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Method that returns WebConstants.WEBAPP_NAME or an empty string if WebConstants.WEBAPP_NAME
	 * is empty.
	 *
	 * @return return WebConstants.WEBAPP_NAME or empty string if WebConstants.WEBAPP_NAME is null
	 * <strong>Should</strong> return empty string if WebConstants.WEBAPP_NAME is null
	 */
	public static String getContextPath() {
		return StringUtils.isEmpty(WebConstants.WEBAPP_NAME) ? "" : "/" + WebConstants.WEBAPP_NAME;
	}
	
	public static String formatDate(Date date) {
		return formatDate(date, Context.getLocale(), FORMAT_TYPE.DATE);
	}
	
	public static String formatDate(Date date, Locale locale, FORMAT_TYPE type) {
		log.debug("Formatting date: " + date + " with locale " + locale);
		
		DateFormat dateFormat = null;
		
		if (type == FORMAT_TYPE.TIMESTAMP) {
			String dateTimeFormat = Context.getAdministrationService().getGlobalPropertyValue(
			    OpenmrsConstants.GP_SEARCH_DATE_DISPLAY_FORMAT, null);
			if (StringUtils.isEmpty(dateTimeFormat)) {
				dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
			} else {
				dateFormat = new OpenmrsDateFormat(new SimpleDateFormat(dateTimeFormat), locale);
			}
		} else if (type == FORMAT_TYPE.TIME) {
			String timeFormat = Context.getAdministrationService().getGlobalPropertyValue(
			    OpenmrsConstants.GP_SEARCH_DATE_DISPLAY_FORMAT, null);
			if (StringUtils.isEmpty(timeFormat)) {
				dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
			} else {
				dateFormat = new OpenmrsDateFormat(new SimpleDateFormat(timeFormat), locale);
			}
		} else if (type == FORMAT_TYPE.DATE) {
			String formatValue = Context.getAdministrationService().getGlobalPropertyValue(
			    OpenmrsConstants.GP_SEARCH_DATE_DISPLAY_FORMAT, "");
			if (StringUtils.isEmpty(formatValue)) {
				dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
			} else {
				dateFormat = new OpenmrsDateFormat(new SimpleDateFormat(formatValue), locale);
			}
		}
		return date == null ? "" : dateFormat.format(date);
	}
	
	/**
	 * @see org.openmrs.api.GlobalPropertyListener#supportsPropertyName(java.lang.String)
	 */
	@Override
	public boolean supportsPropertyName(String propertyName) {
		return OpenmrsConstants.GP_SEARCH_DATE_DISPLAY_FORMAT.equals(propertyName);
	}
	
	/**
	 * @see org.openmrs.api.GlobalPropertyListener#globalPropertyChanged(org.openmrs.GlobalProperty)
	 */
	@Override
	public void globalPropertyChanged(GlobalProperty newValue) {
	}
	
	/**
	 * @see org.openmrs.api.GlobalPropertyListener#globalPropertyDeleted(java.lang.String)
	 */
	@Override
	public void globalPropertyDeleted(String propertyName) {
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/WebDaemon.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import javax.servlet.ServletContext;

import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.ModuleException;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.InputRequiredException;

/**
 * This class provides {@link Daemon} functionality in a web context.
 * 
 * @since 1.9
 */
public class WebDaemon extends Daemon {
	
	/**
	 * Start openmrs in a new thread that is authenticated as the daemon user.
	 * 
	 * @param servletContext the servlet context.
	 */
	public static void startOpenmrs(final ServletContext servletContext) throws DatabaseUpdateException,
	        InputRequiredException {
		
		// create a new thread and start openmrs in it.
		DaemonThread startOpenmrsThread = new DaemonThread() {
			
			@Override
			public void run() {
				isDaemonThread.set(true);
				try {
					Listener.startOpenmrs(servletContext);
				}
				catch (Exception e) {
					exceptionThrown = e;
				}
				finally {
					try {
						Context.closeSession();
					} finally {
						isDaemonThread.remove();
					}
				}
			}
		};
		
		startOpenmrsThread.start();
		
		// wait for the "startOpenmrs" thread to finish
		try {
			startOpenmrsThread.join();
		}
		catch (InterruptedException e) {
			// ignore
		}
		
		if (startOpenmrsThread.getExceptionThrown() != null) {
			throw new ModuleException("Unable to start OpenMRS. Error thrown was: "
			        + startOpenmrsThread.getExceptionThrown().getMessage(), startOpenmrsThread.getExceptionThrown());
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/WebConstants.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

public class WebConstants {
	
	/**
	 * Private constructor to prevent accidental instantiation of this utility class
	 */
	private WebConstants() {
	}
	
	public static final String INIT_REQ_UNIQUE_ID = "__INIT_REQ_UNIQUE_ID__";
	
	public static final String OPENMRS_CONTEXT_HTTPSESSION_ATTR = "__openmrs_context";
	
	public static final String OPENMRS_USER_CONTEXT_HTTPSESSION_ATTR = "__openmrs_user_context";
	
	public static final String OPENMRS_CLIENT_IP_HTTPSESSION_ATTR = "__openmrs_client_ip";
	
	public static final String OPENMRS_LOGIN_REDIRECT_HTTPSESSION_ATTR = "__openmrs_login_redirect";
	
	public static final String OPENMRS_MSG_ATTR = "openmrs_msg";
	
	public static final String OPENMRS_MSG_ARGS = "openmrs_msg_arguments";
	
	public static final String OPENMRS_ERROR_ATTR = "openmrs_error";
	
	public static final String OPENMRS_ERROR_ARGS = "openmrs_error_arguments";
	
	public static final String OPENMRS_ADDR_TMPL = "openmrs_address_template";
	
	public static final String OPENMRS_LANGUAGE_COOKIE_NAME = "__openmrs_language";
	
	public static final String OPENMRS_USER_OVERRIDE_PARAM = "__openmrs_user_over_id";
	
	public static final String OPENMRS_ANALYSIS_IN_PROGRESS_ATTR = "__openmrs_analysis_in_progress";
	
	public static final String OPENMRS_DYNAMIC_FORM_IN_PROGRESS_ATTR = "__openmrs_dynamic_form_in_progress";
	
	public static final String OPENMRS_PATIENT_SET_ATTR = "__openmrs_patient_set";
	
	public static final Integer OPENMRS_PATIENTSET_PAGE_SIZE = 25;
	
	public static final String OPENMRS_DYNAMIC_FORM_KEEPALIVE = "__openmrs_dynamic_form_keepalive";
	
	public static final String OPENMRS_HEADER_USE_MINIMAL = "__openmrs_use_minimal_header";
	
	public static final String OPENMRS_PORTLET_MODEL_NAME = "model";
	
	public static final String OPENMRS_PORTLET_LAST_REQ_ID = "__openmrs_portlet_last_req_id";
	
	public static final String OPENMRS_PORTLET_CACHED_MODEL = "__openmrs_portlet_cached_model";
	
	// these vars filled in by org.openmrs.web.Listener at webapp start time
	public static String BUILD_TIMESTAMP = "";
	
	public static String WEBAPP_NAME = "openmrs";
	
	/**
	 * Page in the webapp used for initial setup of the database connection if no valid one exists
	 */
	public static final String SETUP_PAGE_URL = "initialsetup";
	
	/**
	 * The url of the module repository. This is filled in at startup by the value in web.xml
	 */
	public static String MODULE_REPOSITORY_URL = "";
	
	/**
	 * Global property name for the number of times one IP can fail at logging in before being
	 * locked out. A value of 0 for this property means no IP lockout checks.
	 * 
	 * @see org.openmrs.web.servlet.LoginServlet
	 */
	public static final String GP_ALLOWED_LOGIN_ATTEMPTS_PER_IP = "security.loginAttemptsAllowedPerIP";
	
	/**
	 * User names of the logged-in users are stored in this map (session id -&gt; user name) in the
	 * ServletContext under this key
	 */
	public static final String CURRENT_USERS = "CURRENT_USERS";
	
	/**
	 * Session attribute name that specifies if there are any privilege checks the currently
	 * authenticated user failed
	 */
	public static final String INSUFFICIENT_PRIVILEGES = "insufficient_privileges";
	
	/**
	 * Session attribute name for the url of the page the user was trying to access when they failed
	 * a privilege check
	 */
	public static final String DENIED_PAGE = "denied_page";
	
	/**
	 * Session attribute name for the privileges the user didn't have
	 */
	public static final String REQUIRED_PRIVILEGES = "required_privileges";
	
	/**
	 * Session attribute name for the uncaught exception message
	 */
	public static final String UNCAUGHT_EXCEPTION_MESSAGE = "uncaught_exception_message";
	
	/**
	 * Session attribute name for the referer url
	 */
	public static final String REFERER_URL = "referer_url";
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/OpenmrsBindingInitializer.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import java.text.NumberFormat;
import java.util.Date;

import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptAttributeType;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNumeric;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.Drug;
import org.openmrs.Encounter;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.Privilege;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.Provider;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.VisitType;
import org.openmrs.api.context.Context;
import org.openmrs.propertyeditor.CohortEditor;
import org.openmrs.propertyeditor.ConceptAnswerEditor;
import org.openmrs.propertyeditor.ConceptAttributeTypeEditor;
import org.openmrs.propertyeditor.ConceptClassEditor;
import org.openmrs.propertyeditor.ConceptDatatypeEditor;
import org.openmrs.propertyeditor.ConceptEditor;
import org.openmrs.propertyeditor.ConceptMapTypeEditor;
import org.openmrs.propertyeditor.ConceptNameEditor;
import org.openmrs.propertyeditor.ConceptNumericEditor;
import org.openmrs.propertyeditor.ConceptReferenceTermEditor;
import org.openmrs.propertyeditor.ConceptSourceEditor;
import org.openmrs.propertyeditor.DateOrDatetimeEditor;
import org.openmrs.propertyeditor.DrugEditor;
import org.openmrs.propertyeditor.EncounterEditor;
import org.openmrs.propertyeditor.FormEditor;
import org.openmrs.propertyeditor.LocationAttributeTypeEditor;
import org.openmrs.propertyeditor.LocationEditor;
import org.openmrs.propertyeditor.LocationTagEditor;
import org.openmrs.propertyeditor.OrderEditor;
import org.openmrs.propertyeditor.PatientEditor;
import org.openmrs.propertyeditor.PatientIdentifierTypeEditor;
import org.openmrs.propertyeditor.PersonAttributeEditor;
import org.openmrs.propertyeditor.PersonAttributeTypeEditor;
import org.openmrs.propertyeditor.PersonEditor;
import org.openmrs.propertyeditor.PrivilegeEditor;
import org.openmrs.propertyeditor.ProgramEditor;
import org.openmrs.propertyeditor.ProgramWorkflowEditor;
import org.openmrs.propertyeditor.ProgramWorkflowStateEditor;
import org.openmrs.propertyeditor.ProviderEditor;
import org.openmrs.propertyeditor.RoleEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.openmrs.propertyeditor.VisitEditor;
import org.openmrs.propertyeditor.VisitTypeEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

/**
 * Shared WebBindingInitializer that allows all OpenMRS annotated controllers to use our custom
 * editors.
 */
public class OpenmrsBindingInitializer implements WebBindingInitializer {
	
	/**
	 * @see org.springframework.web.bind.support.WebBindingInitializer#initBinder(org.springframework.web.bind.WebDataBinder,
	 *      org.springframework.web.context.request.WebRequest)
	 */
	@Override
	public void initBinder(WebDataBinder wdb) {
		wdb.registerCustomEditor(Cohort.class, new CohortEditor());
		wdb.registerCustomEditor(Concept.class, new ConceptEditor());
		wdb.registerCustomEditor(ConceptAnswer.class, new ConceptAnswerEditor());
		wdb.registerCustomEditor(ConceptClass.class, new ConceptClassEditor());
		wdb.registerCustomEditor(ConceptDatatype.class, new ConceptDatatypeEditor());
		wdb.registerCustomEditor(ConceptName.class, new ConceptNameEditor());
		wdb.registerCustomEditor(ConceptNumeric.class, new ConceptNumericEditor());
		wdb.registerCustomEditor(ConceptSource.class, new ConceptSourceEditor());
		wdb.registerCustomEditor(Drug.class, new DrugEditor());
		wdb.registerCustomEditor(Encounter.class, new EncounterEditor());
		wdb.registerCustomEditor(Form.class, new FormEditor());
		wdb.registerCustomEditor(Location.class, new LocationEditor());
		wdb.registerCustomEditor(LocationTag.class, new LocationTagEditor());
		wdb.registerCustomEditor(LocationAttributeType.class, new LocationAttributeTypeEditor());
		wdb.registerCustomEditor(Order.class, new OrderEditor());
		wdb.registerCustomEditor(Patient.class, new PatientEditor());
		wdb.registerCustomEditor(PatientIdentifierType.class, new PatientIdentifierTypeEditor());
		wdb.registerCustomEditor(PersonAttribute.class, new PersonAttributeEditor());
		wdb.registerCustomEditor(PersonAttributeType.class, new PersonAttributeTypeEditor());
		wdb.registerCustomEditor(Person.class, new PersonEditor());
		wdb.registerCustomEditor(Privilege.class, new PrivilegeEditor());
		wdb.registerCustomEditor(Program.class, new ProgramEditor());
		wdb.registerCustomEditor(ProgramWorkflow.class, new ProgramWorkflowEditor());
		wdb.registerCustomEditor(ProgramWorkflowState.class, new ProgramWorkflowStateEditor());
		wdb.registerCustomEditor(Provider.class, new ProviderEditor());
		wdb.registerCustomEditor(Role.class, new RoleEditor());
		wdb.registerCustomEditor(User.class, new UserEditor());
		wdb.registerCustomEditor(java.lang.Integer.class, new CustomNumberEditor(java.lang.Integer.class, NumberFormat
		        .getInstance(Context.getLocale()), true));
		wdb.registerCustomEditor(Date.class, new DateOrDatetimeEditor());
		wdb.registerCustomEditor(PatientIdentifierType.class, new PatientIdentifierTypeEditor());
		wdb.registerCustomEditor(ConceptMapType.class, new ConceptMapTypeEditor());
		wdb.registerCustomEditor(ConceptSource.class, new ConceptSourceEditor());
		wdb.registerCustomEditor(ConceptReferenceTerm.class, new ConceptReferenceTermEditor());
		wdb.registerCustomEditor(ConceptAttributeType.class, new ConceptAttributeTypeEditor());
		wdb.registerCustomEditor(VisitType.class, new VisitTypeEditor());
		wdb.registerCustomEditor(Visit.class, new VisitEditor());
		
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/Listener.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import org.apache.logging.log4j.LogManager;
import org.openmrs.api.context.Context;
import org.openmrs.logging.OpenmrsLoggingUtil;
import org.openmrs.module.MandatoryModuleException;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.ModuleMustStartException;
import org.openmrs.module.OpenmrsCoreModuleException;
import org.openmrs.module.web.OpenmrsJspServlet;
import org.openmrs.module.web.WebModuleUtil;
import org.openmrs.scheduler.SchedulerUtil;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.DatabaseUpdater;
import org.openmrs.util.InputRequiredException;
import org.openmrs.util.MemoryLeakUtil;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.filter.initialization.DatabaseDetective;
import org.openmrs.web.filter.initialization.InitializationFilter;
import org.openmrs.web.filter.update.UpdateFilter;
import org.owasp.csrfguard.CsrfGuard;
import org.owasp.csrfguard.CsrfGuardServletContextListener;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Our Listener class performs the basic starting functions for our webapp. Basic needs for starting
 * the API: 1) Get the runtime properties 2) Start Spring 3) Start the OpenMRS APi (via
 * Context.startup) Basic startup needs specific to the web layer: 1) Do the web startup of the
 * modules 2) Copy the custom look/images/messages over into the web layer
 */
public final class Listener extends ContextLoader implements ServletContextListener, HttpSessionListener {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(Listener.class);
	
	private static boolean runtimePropertiesFound = false;
	
	private static Throwable errorAtStartup = null;
	
	private static boolean setupNeeded = false;
	
	private static boolean openmrsStarted = false;
	
	/**
	 * Boolean flag set on webapp startup marking whether there is a runtime properties file or not.
	 * If there is not, then the {@link InitializationFilter} takes over any openmrs url and
	 * redirects to the {@link WebConstants#SETUP_PAGE_URL}
	 *
	 * @return true/false whether an openmrs runtime properties file is defined
	 */
	public static boolean runtimePropertiesFound() {
		return runtimePropertiesFound;
	}
	
	/**
	 * Boolean flag set by the {@link #contextInitialized(ServletContextEvent)} method if an error
	 * occurred when trying to start up. The StartupErrorFilter displays the error to the admin
	 *
	 * @return true/false if an error occurred when starting up
	 */
	public static boolean errorOccurredAtStartup() {
		return errorAtStartup != null;
	}
	
	/**
	 * Boolean flag that tells if we need to run the database setup wizard.
	 *
	 * @return true if setup is needed, else false.
	 */
	public static boolean isSetupNeeded() {
		return setupNeeded;
	}
	
	/**
	 * Boolean flag that tells if OpenMRS is started and ready to handle requests via REST.
	 *
	 * @return true if started, else false.
	 */
	public static boolean isOpenmrsStarted() {
		return openmrsStarted;
	}
	
	/**
	 * Get the error thrown at startup
	 *
	 * @return get the error thrown at startup
	 */
	public static Throwable getErrorAtStartup() {
		return errorAtStartup;
	}
	
	public static void setRuntimePropertiesFound(boolean runtimePropertiesFound) {
		Listener.runtimePropertiesFound = runtimePropertiesFound;
	}
	
	public static void setErrorAtStartup(Throwable errorAtStartup) {
		Listener.errorAtStartup = errorAtStartup;
	}

	/**
	 * This gets all Spring components that implement HttpSessionListener 
	 * and passes the HttpSession event to them whenever an HttpSession is created
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent) 
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		for (HttpSessionListener listener : getHttpSessionListeners()) {
			listener.sessionCreated(se);
		}
	}

	/**
	 * 	This gets all Spring components that implement HttpSessionListener 
	 * 	and passes the HttpSession event to them whenever an HttpSession is destroyed
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		for (HttpSessionListener listener : getHttpSessionListeners()) {
			listener.sessionDestroyed(se);
		}
	}

	/**
	 * 	This retrieves all Spring components that implement HttpSessionListener
	 * 	If an exception is thrown trying to retrieve these beans from the Context, a warning is logged
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	private List<HttpSessionListener> getHttpSessionListeners() {
		List<HttpSessionListener> httpSessionListeners = Collections.emptyList();
		
		if (openmrsStarted) {
			try {
				httpSessionListeners = Context.getRegisteredComponents(HttpSessionListener.class);
			}
			catch (Exception e) {
				log.warn("An error occurred trying to retrieve HttpSessionListener beans from the context", e);
			}
		}
		
		return httpSessionListeners;
	}

	/**
	 * This method is called when the servlet context is initialized(when the Web Application is
	 * deployed). You can initialize servlet context related data here.
	 *
	 * @param event
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.debug("Starting the OpenMRS webapp");
		
		try {
			// validate the current JVM version
			OpenmrsUtil.validateJavaVersion();
			
			ServletContext servletContext = event.getServletContext();
			
			// pulled from web.xml.
			loadConstants(servletContext);
			
			// erase things in the dwr file
			clearDWRFile(servletContext);
			
			setApplicationDataDirectory(servletContext);
			
			
			// Try to get the runtime properties
			Properties props = getRuntimeProperties();
			if (props != null) {
				// the user has defined a runtime properties file
				setRuntimePropertiesFound(true);
				// set props to the context so that they can be
				// used during sessionFactory creation
				Context.setRuntimeProperties(props);
				
				String appDataRuntimeProperty = props
				        .getProperty(OpenmrsConstants.APPLICATION_DATA_DIRECTORY_RUNTIME_PROPERTY, null);
				if (StringUtils.hasLength(appDataRuntimeProperty)) {
					OpenmrsUtil.setApplicationDataDirectory(null);
				}
				
				//ensure that we always log the runtime properties file that we are using
				//since openmrs is just booting, the log levels are not yet set. TRUNK-4835
				OpenmrsLoggingUtil.applyLogLevel(getClass().toString(), "INFO");
				log.info("Using runtime properties file: {}",
				         OpenmrsUtil.getRuntimePropertiesFilePathName(WebConstants.WEBAPP_NAME));
			}

			loadCsrfGuardProperties(servletContext);
			
			Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
			
			if (!setupNeeded()) {
				// must be done after the runtime properties are
				// found but before the database update is done
				copyCustomizationIntoWebapp(servletContext, props);
				
				/**
				 * This logic is from ContextLoader.initWebApplicationContext. Copied here instead
				 * of calling that so that the context is not cached and hence not garbage collected
				 */
				XmlWebApplicationContext context = (XmlWebApplicationContext) createWebApplicationContext(servletContext);
				configureAndRefreshWebApplicationContext(context, servletContext);
				servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
				
				WebDaemon.startOpenmrs(event.getServletContext());
			} else {
				setupNeeded = true;
			}
			
		}
		catch (Exception e) {
			setErrorAtStartup(e);
			log.error(MarkerFactory.getMarker("FATAL"), "Failed to obtain JDBC connection", e);
		}
	}

	private void loadCsrfGuardProperties(ServletContext servletContext) throws IOException {
		File csrfGuardFile = new File(OpenmrsUtil.getApplicationDataDirectory(), "csrfguard.properties");
		Properties csrfGuardProperties = new Properties();
		if (csrfGuardFile.exists()) {
			try (InputStream csrfGuardInputStream = Files.newInputStream(csrfGuardFile.toPath())) {
				csrfGuardProperties.load(csrfGuardInputStream);
			}
			catch (Exception e) {
				log.error("Error loading csrfguard.properties file at " + csrfGuardFile.getAbsolutePath(), e);
				throw e;
			}
		}
		else {
			String fileName = servletContext.getRealPath("/WEB-INF/csrfguard.properties");
			try (InputStream csrfGuardInputStream = Files.newInputStream(Paths.get(fileName))) {
				csrfGuardProperties.load(csrfGuardInputStream);
			}
			catch (Exception e) {
				log.error("Error loading csrfguard.properties file at " +  fileName, e);
				throw e;
			}
		}
		
		Properties runtimeProperties = getRuntimeProperties();
		if (runtimeProperties != null) {
			runtimeProperties.stringPropertyNames().forEach(property -> {
				if (property.startsWith("org.owasp.csrfguard")) {
					csrfGuardProperties.setProperty(property, runtimeProperties.getProperty(property));
				}
			});	
		}
		
		CsrfGuard.load(csrfGuardProperties);
		
		try {
			//CSRFGuard by default loads properties using CsrfGuardServletContextListener
			//which sets the servlet context path to be used during variable substitution of
			//%servletContext% in the properties file.
			Field field = CsrfGuardServletContextListener.class.getDeclaredField("servletContext");
			field.setAccessible(true);
			field.set(null, servletContext.getContextPath());
		}
		catch (Exception ex) {
			log.error("Failed to set the CSRFGuard servlet context", ex);
		}
	}
	
	/**
	 * This method knows about all the filters that openmrs uses for setup. Currently those are the
	 * {@link InitializationFilter} and the {@link UpdateFilter}. If either of these have to do
	 * something, openmrs won't start in this Listener.
	 *
	 * @return true if one of the filters needs to take some action
	 */
	private boolean setupNeeded() throws Exception {
		if (!runtimePropertiesFound) {
			return true;
		}
		
		DatabaseDetective databaseDetective = new DatabaseDetective();
		if (databaseDetective.isDatabaseEmpty(OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME))) {
			return true;
		}
		
		return DatabaseUpdater.updatesRequired() && !DatabaseUpdater.allowAutoUpdate();
	}
	
	/**
	 * Do the work of starting openmrs.
	 *
	 * @param servletContext
	 * @throws ServletException
	 */
	public static void startOpenmrs(ServletContext servletContext) throws ServletException {
		openmrsStarted = false;
		// start openmrs
		try {
			// load bundled modules that are packaged into the webapp
			Listener.loadBundledModules(servletContext);
			
			Context.startup(getRuntimeProperties());
		}
		catch (DatabaseUpdateException | InputRequiredException updateEx) {
			throw new ServletException("Should not be here because updates were run previously", updateEx);
		}
		catch (MandatoryModuleException mandatoryModEx) {
			throw new ServletException(mandatoryModEx);
		}
		catch (OpenmrsCoreModuleException coreModEx) {
			// don't wrap this error in a ServletException because we want to deal with it differently
			// in the StartupErrorFilter class
			throw coreModEx;
		}
		
		// TODO catch openmrs errors here and drop the user back out to the setup screen
		
		try {
			
			// web load modules
			Listener.performWebStartOfModules(servletContext);
			
			// start the scheduled tasks
			SchedulerUtil.startup(getRuntimeProperties());
		}
		catch (Exception t) {
			Context.shutdown();
			WebModuleUtil.shutdownModules(servletContext);
			throw new ServletException(t);
		}
		finally {
			Context.closeSession();
		}
		openmrsStarted = true;
	}
	
	/**
	 * Load the openmrs constants with values from web.xml init parameters
	 *
	 * @param servletContext startup context (web.xml)
	 */
	private void loadConstants(ServletContext servletContext) {
		WebConstants.BUILD_TIMESTAMP = servletContext.getInitParameter("build.timestamp");
		WebConstants.WEBAPP_NAME = getContextPath(servletContext);
		WebConstants.MODULE_REPOSITORY_URL = servletContext.getInitParameter("module.repository.url");
		
		if (!"openmrs".equalsIgnoreCase(WebConstants.WEBAPP_NAME)) {
			OpenmrsConstants.KEY_OPENMRS_APPLICATION_DATA_DIRECTORY = WebConstants.WEBAPP_NAME
			        + "_APPLICATION_DATA_DIRECTORY";
		}
	}
	
	private void setApplicationDataDirectory(ServletContext servletContext) {
		// note: the below value will be overridden after reading the runtime properties if the
		// "application_data_directory" runtime property is set
		String appDataDir = servletContext.getInitParameter("application.data.directory");
		if (StringUtils.hasLength(appDataDir)) {
			OpenmrsUtil.setApplicationDataDirectory(appDataDir);
		} else if (!"openmrs".equalsIgnoreCase(WebConstants.WEBAPP_NAME)) {
			OpenmrsUtil.setApplicationDataDirectory(
			    Paths.get(OpenmrsUtil.getApplicationDataDirectory(), WebConstants.WEBAPP_NAME).toString());
		}
	}
	
	/**
	 * @return current contextPath of this webapp without initial slash
	 */
	private String getContextPath(ServletContext servletContext) {
		// Get the context path without the request.
		String contextPath = servletContext.getContextPath();
		
		// trim off initial slash if it exists
		if (contextPath.startsWith("/")) {
			contextPath = contextPath.substring(1);
		}
		
		return contextPath;
	}
	
	/**
	 * Convenience method to empty out the dwr-modules.xml file to fix any errors that might have
	 * occurred in it when loading or unloading modules.
	 *
	 * @param servletContext
	 */
	private void clearDWRFile(ServletContext servletContext) {
		File dwrFile = Paths.get(servletContext.getRealPath(""), "WEB-INF", "dwr-modules.xml").toFile();
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// When asked to resolve external entities (such as a DTD) we return an InputSource
			// with no data at the end, causing the parser to ignore the DTD.
			db.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
			Document doc = db.parse(dwrFile);
			Element elem = doc.getDocumentElement();
			elem.setTextContent("");
			OpenmrsUtil.saveDocument(doc, dwrFile);
		}
		catch (Exception e) {
			// got here because the dwr-modules.xml file is empty for some reason.  This might
			// happen because the servlet container (i.e. tomcat) crashes when first loading this file
			log.debug("Error clearing dwr-modules.xml", e);
			dwrFile.delete();
			OutputStreamWriter writer = null;
			try {
				writer = new OutputStreamWriter(new FileOutputStream(dwrFile), StandardCharsets.UTF_8);
				writer.write(
				    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE dwr PUBLIC \"-//GetAhead Limited//DTD Direct Web Remoting 2.0//EN\" \"http://directwebremoting.org/schema/dwr20.dtd\">\n<dwr></dwr>");
			}
			catch (IOException io) {
				log.error(
				    "Unable to clear out the " + dwrFile.getAbsolutePath() + " file.  Please redeploy the openmrs war file",
				    io);
			}
			finally {
				if (writer != null) {
					try {
						writer.close();
					}
					catch (IOException io) {
						log.warn("Couldn't close Writer: " + io);
					}
				}
			}
		}
	}
	
	/**
	 * Copy the customization scripts over into the webapp
	 *
	 * @param servletContext
	 */
	private void copyCustomizationIntoWebapp(ServletContext servletContext, Properties props) {
		String realPath = servletContext.getRealPath("");
		// TODO centralize map to WebConstants?
		Map<String, String> custom = new HashMap<>();
		custom.put("custom.template.dir", "/WEB-INF/template");
		custom.put("custom.index.jsp.file", "/WEB-INF/view/index.jsp");
		custom.put("custom.login.jsp.file", "/WEB-INF/view/login.jsp");
		custom.put("custom.patientDashboardForm.jsp.file", "/WEB-INF/view/patientDashboardForm.jsp");
		custom.put("custom.images.dir", "/images");
		custom.put("custom.style.css.file", "/style.css");
		custom.put("custom.messages", "/WEB-INF/custom_messages.properties");
		custom.put("custom.messages_fr", "/WEB-INF/custom_messages_fr.properties");
		custom.put("custom.messages_es", "/WEB-INF/custom_messages_es.properties");
		custom.put("custom.messages_de", "/WEB-INF/custom_messages_de.properties");
		
		for (Map.Entry<String, String> entry : custom.entrySet()) {
			String prop = entry.getKey();
			String webappPath = entry.getValue();
			String userOverridePath = props.getProperty(prop);
			// if they defined the variable
			if (userOverridePath != null) {
				String absolutePath = realPath + webappPath;
				File file = new File(userOverridePath);
				
				// if they got the path correct
				// also, if file does not start with a "." (hidden files, like SVN files)
				if (file.exists() && !userOverridePath.startsWith(".")) {
					log.debug("Overriding file: " + absolutePath);
					log.debug("Overriding file with: " + userOverridePath);
					if (file.isDirectory()) {
						File[] files = file.listFiles();
						if (files != null) {
							for (File f : files) {
								userOverridePath = f.getAbsolutePath();
								if (!f.getName().startsWith(".")) {
									String tmpAbsolutePath = absolutePath + "/" + f.getName();
									if (!copyFile(userOverridePath, tmpAbsolutePath)) {
										log.warn("Unable to copy file in folder defined by runtime property: " + prop);
										log.warn("Your source directory (or a file in it) '" + userOverridePath
													+ " cannot be loaded or destination '" + tmpAbsolutePath + "' cannot be found");
									}
								}
							}
						}
					} else {
						// file is not a directory
						if (!copyFile(userOverridePath, absolutePath)) {
							log.warn("Unable to copy file defined by runtime property: " + prop);
							log.warn("Your source file '" + userOverridePath + " cannot be loaded or destination '"
							        + absolutePath + "' cannot be found");
						}
					}
				}
			}
			
		}
	}
	
	/**
	 * Copies file pointed to by <code>fromPath</code> to <code>toPath</code>
	 *
	 * @param fromPath
	 * @param toPath
	 * @return true/false whether the copy was a success
	 */
	private boolean copyFile(String fromPath, String toPath) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(fromPath);
			outputStream = new FileOutputStream(toPath);
			OpenmrsUtil.copyFile(inputStream, outputStream);
		}
		catch (IOException io) {
			return false;
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (IOException io) {
				log.warn("Unable to close input stream", io);
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			}
			catch (IOException io) {
				log.warn("Unable to close input stream", io);
			}
		}
		return true;
	}
	
	/**
	 * Load the pre-packaged modules from web/WEB-INF/bundledModules. <br>
	 * <br>
	 * This method assumes that the api startup() and WebModuleUtil.startup() will be called later
	 * for modules that loaded here
	 *
	 * @param servletContext the current servlet context for the webapp
	 */
	public static void loadBundledModules(ServletContext servletContext) {
		File folder = Paths.get(servletContext.getRealPath(""), "WEB-INF", "bundledModules").toFile();
		
		if (!folder.exists()) {
			log.warn("Bundled module folder doesn't exist: " + folder.getAbsolutePath());
			return;
		}
		if (!folder.isDirectory()) {
			log.warn("Bundled module folder isn't really a directory: " + folder.getAbsolutePath());
			return;
		}
		
		// loop over the modules and load the modules that we can
		File[] files = folder.listFiles();
		if (files != null) {
			for (File f : files) {
				if (!f.getName().startsWith(".")) { // ignore .svn folder and the like
					try {
						Module mod = ModuleFactory.loadModule(f);
						log.debug("Loaded bundled module: " + mod + " successfully");
					}
					catch (Exception e) {
						log.warn("Error while trying to load bundled module " + f.getName() + "", e);
					}
				}
			}
		}
	}
	
	/**
	 * Called when the webapp is shut down properly Must call Context.shutdown() and then shutdown
	 * all the web layers of the modules
	 *
	 * @see org.springframework.web.context.ContextLoaderListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@SuppressWarnings("squid:S1215")
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
		try {
			openmrsStarted = false;
			Context.openSession();
			
			Context.shutdown();
			
			WebModuleUtil.shutdownModules(event.getServletContext());
			
		}
		catch (Exception e) {
			// don't print the unhelpful "contextDAO is null" message
			if (!"contextDAO is null".equals(e.getMessage())) {
				// not using log.error here so it can be garbage collected
				System.out.println("Listener.contextDestroyed: Error while shutting down openmrs: ");
				log.error("Listener.contextDestroyed: Error while shutting down openmrs: ", e);
			}
		}
		finally {
			if ("true".equalsIgnoreCase(System.getProperty("FUNCTIONAL_TEST_MODE"))) {
				//Delete the temporary file created for functional testing and shutdown the mysql daemon
				String filename = WebConstants.WEBAPP_NAME + "-test-runtime.properties";
				File file = new File(OpenmrsUtil.getApplicationDataDirectory(), filename);
				System.out.println(filename + " delete=" + file.delete());
				
			}
			// remove the user context that we set earlier
			Context.closeSession();
		}
		try {
			for (Enumeration<Driver> e = DriverManager.getDrivers(); e.hasMoreElements();) {
				Driver driver = e.nextElement();
				ClassLoader classLoader = driver.getClass().getClassLoader();
				// only unload drivers for this webapp
				if (classLoader == null || classLoader == getClass().getClassLoader()) {
					DriverManager.deregisterDriver(driver);
				} else {
					System.err.println("Didn't remove driver class: " + driver.getClass() + " with classloader of: "
					        + driver.getClass().getClassLoader());
				}
			}
		}
		catch (Exception e) {
			System.err.println("Listener.contextDestroyed: Failed to cleanup drivers in webapp");
			log.error("Listener.contextDestroyed: Failed to cleanup drivers in webapp", e);
		}
		
		MemoryLeakUtil.shutdownMysqlCancellationTimer();
		
		OpenmrsClassLoader.onShutdown();
		
		LogManager.shutdown();
		
		// just to make things nice and clean.
		// Suppressing sonar issue squid:S1215
		System.gc();
		System.gc();
	}
	
	/**
	 * Finds and loads the runtime properties
	 *
	 * @return Properties
	 * @see OpenmrsUtil#getRuntimeProperties(String)
	 */
	public static Properties getRuntimeProperties() {
		return OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME);
	}
	
	/**
	 * Call WebModuleUtil.startModule on each started module
	 *
	 * @param servletContext
	 * @throws ModuleMustStartException if the context cannot restart due to a
	 *             {@link MandatoryModuleException} or {@link OpenmrsCoreModuleException}
	 */
	public static void performWebStartOfModules(ServletContext servletContext) throws ModuleMustStartException, Exception {
		List<Module> startedModules = new ArrayList<>(ModuleFactory.getStartedModules());
		performWebStartOfModules(startedModules, servletContext);
	}
	
	public static void performWebStartOfModules(Collection<Module> startedModules, ServletContext servletContext)
	        throws ModuleMustStartException, Exception {
		
		boolean someModuleNeedsARefresh = false;
		for (Module mod : startedModules) {
			try {
				boolean thisModuleCausesRefresh = WebModuleUtil.startModule(mod, servletContext,
				    /* delayContextRefresh */true);
				someModuleNeedsARefresh = someModuleNeedsARefresh || thisModuleCausesRefresh;
			}
			catch (Exception e) {
				mod.setStartupErrorMessage("Unable to start module", e);
			}
		}
		
		if (someModuleNeedsARefresh) {
			try {
				WebModuleUtil.refreshWAC(servletContext, true, null);
			}
			catch (ModuleMustStartException | BeanCreationException ex) {
				// pass this up to the calling method so that openmrs loading stops
				throw ex;
			}
			catch (Exception e) {
				Throwable rootCause = getActualRootCause(e, true);
				if (rootCause != null) {
					log.error(MarkerFactory.getMarker("FATAL"),
					    "Unable to refresh the spring application context.  Root Cause was:", rootCause);
				} else {
					log.error(MarkerFactory.getMarker("FATAL"),
					    "nable to refresh the spring application context. Unloading all modules,  Error was:", e);
				}
				
				try {
					WebModuleUtil.shutdownModules(servletContext);
					for (Module mod : ModuleFactory.getLoadedModules()) {// use loadedModules to avoid a concurrentmodificationexception
						if (!mod.isCoreModule() && !mod.isMandatory()) {
							try {
								ModuleFactory.stopModule(mod, true, true);
							}
							catch (Exception t3) {
								// just keep going if we get an error shutting down.  was probably caused by the module
								// that actually got us to this point!
								log.trace("Unable to shutdown module:" + mod, t3);
							}
						}
					}
					WebModuleUtil.refreshWAC(servletContext, true, null);
				}
				catch (MandatoryModuleException ex) {
					// pass this up to the calling method so that openmrs loading stops
					throw new MandatoryModuleException(ex.getModuleId(), "Got an error while starting a mandatory module: "
					        + e.getMessage() + ". Check the server logs for more information");
				}
				catch (Exception t2) {
					// a mandatory or core module is causing spring to fail to start up.  We don't want those
					// stopped so we must report this error to the higher authorities
					log.warn("caught another error: ", t2);
					throw t2;
				}
			}
		}
		
		// because we delayed the refresh, we need to load+start all servlets and filters now
		// (this is to protect servlets/filters that depend on their module's spring xml config being available)
		for (Module mod : ModuleFactory.getStartedModulesInOrder()) {
			WebModuleUtil.loadServlets(mod, servletContext);
			WebModuleUtil.loadFilters(mod, servletContext);
		}
		servletContext.setAttribute(OpenmrsJspServlet.OPENMRS_TLD_SCAN_NEEDED, true);
	}
	
	/**
	 * Convenience method that recursively attempts to pull the root case from a Throwable
	 *
	 * @param t the Throwable object
	 * @param isOriginalError specifies if the passed in Throwable is the original Exception that
	 *            was thrown
	 * @return the root cause if any was found
	 */
	private static Throwable getActualRootCause(Throwable t, boolean isOriginalError) {
		if (t.getCause() != null) {
			return getActualRootCause(t.getCause(), false);
		}
		
		if (!isOriginalError) {
			return t;
		}
		
		return null;
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/StaticDispatcherServlet.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import javax.servlet.ServletException;

import org.openmrs.module.web.WebModuleUtil;
import org.openmrs.util.OpenmrsClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * This class is only used to get access to the dispatcher servlet that handles static content. <br>
 * <br>
 * After creation, this object is saved to WebModuleUtil for later use. When Spring's root 
 * webApplicationContext is refreshed, this dispatcher servlet needs to be refreshed too.
 */
public class StaticDispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(StaticDispatcherServlet.class);
	
	/**
	 * @see org.springframework.web.servlet.FrameworkServlet#initFrameworkServlet()
	 */
	@Override
	protected void initFrameworkServlet() throws ServletException, BeansException {
		
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		
		log.info("Framework being initialized for static content");
		WebModuleUtil.setStaticDispatcherServlet(this);
		
		super.initFrameworkServlet();
	}
	
	/**
	 * Called by the ModuleUtil after adding in a new, updating, starting, or stopping a module.
	 * This needs to be called because each spring dispatcher servlet creates a new application
	 * context, which therefore needs to be refreshed too.
	 * 
	 * @throws ServletException
	 */
	public void refreshApplicationContext() throws ServletException {
		log.info("Application context for the static content dispatcher servlet is being refreshed");
		
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		((XmlWebApplicationContext) getWebApplicationContext()).setClassLoader(OpenmrsClassLoader.getInstance());
		
		refresh();
	}
	
	public void stopAndCloseApplicationContext() {
		try {
			XmlWebApplicationContext ctx = (XmlWebApplicationContext) getWebApplicationContext();
			ctx.stop();
			ctx.close();
		}
		catch (Exception e) {
			log.error("Exception while stopping and closing static content dispatcher servlet context: ", e);
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/DispatcherServlet.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.web.WebModuleUtil;
import org.openmrs.util.DatabaseUpdater;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.web.filter.initialization.InitializationFilter;
import org.openmrs.web.filter.update.UpdateFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * This class is only used to get access to the DispatcherServlet. <br>
 * <br>
 * After creation, this object is saved to WebUtil for later use. When Spring's
 * webApplicationContext is refreshed, the DispatcherServlet needs to be refreshed too.
 * 
 * @see #reInitFrameworkServlet()
 */
public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {
	
	private static final long serialVersionUID = -6925172744402818729L;
	
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
	
	/**
	 * @see org.springframework.web.servlet.FrameworkServlet#initFrameworkServlet()
	 */
	@Override
	protected void initFrameworkServlet() throws ServletException, BeansException {
		// refresh the application context to look for module xml config files as well
		
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		
		log.debug("Framework being initialized");
		WebModuleUtil.setDispatcherServlet(this);
		
		super.initFrameworkServlet();
	}
	
	/**
	 * Called by the ModuleUtil after adding in a new module. This needs to be called because the
	 * new mappings and advice that a new module adds in are cached by Spring's DispatcherServlet.
	 * This method will reload that cache.
	 * 
	 * @throws ServletException
	 */
	public void reInitFrameworkServlet() throws ServletException {
		log.debug("Framework being REinitialized");
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		((XmlWebApplicationContext) getWebApplicationContext()).setClassLoader(OpenmrsClassLoader.getInstance());
		
		init();
		
		// the spring context gets reset by the framework servlet, so we need to 
		// reload the advice points that were lost when refreshing Spring
		for (Module module : ModuleFactory.getStartedModules()) {
			ModuleFactory.loadAdvice(module);
		}
	}
	
	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		// hacky way to know if one of the startup filters needs to be run
		if (UpdateFilter.updatesRequired() && !DatabaseUpdater.allowAutoUpdate()) {
			log.info("DB updates are required, the update wizard must be run");
		}
		if (InitializationFilter.initializationRequired()) {
			log.info("Runtime properties were not found or the database is empty, so initialization is required");
		}
	}
	
	/**
	 * Stops and closes the application context created by this dispatcher servlet.
	 */
	public void stopAndCloseApplicationContext() {
		try {
			XmlWebApplicationContext ctx = (XmlWebApplicationContext) getWebApplicationContext();
			ctx.stop();
			ctx.close();
		}
		catch (Exception e) {
			log.error("Exception while stopping and closing dispatcherServlet context: ", e);
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/controller/PseudoStaticContentController.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.GlobalProperty;
import org.openmrs.api.GlobalPropertyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.LastModified;

/**
 * This controller basically passes requests straight through to their views. When interpretJstl is
 * enabled, ".withjstl" is appended to the view name. (This allows us to use jstl (such as the
 * spring:message tag) in some javascript files.) <br>
 * If you specify any 'rewrites' then the specified paths are remapped, e.g:<br>
 * /scripts/jquery/jquery-1.3.2.min.js -&gt; /scripts/jquery/jquery.min.js <br>
 * All jstl files are cached in the browser until a server restart or a global property is
 * added/changed/deleted
 */
public class PseudoStaticContentController implements Controller, LastModified, GlobalPropertyListener {
	
	private static final Logger log = LoggerFactory.getLogger(PseudoStaticContentController.class);
	
	private Boolean interpretJstl = false;
	
	private Map<String, String> rewrites;
	
	private static Long lastModified = System.currentTimeMillis();
	
	public Boolean getInterpretJstl() {
		return interpretJstl;
	}
	
	public void setInterpretJstl(Boolean interpretJstl) {
		this.interpretJstl = interpretJstl;
	}
	
	public Map<String, String> getRewrites() {
		return rewrites;
	}
	
	public void setRewrites(Map<String, String> rewrites) {
		this.rewrites = rewrites;
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	        IOException {
		String path = request.getServletPath() + request.getPathInfo();
		
		if (rewrites != null && rewrites.containsKey(path)) {
			path = rewrites.get(path);
		}
		if (interpretJstl) {
			path += ".withjstl";
		}
		
		return new ModelAndView(path);
	}
	
	@Override
	public long getLastModified(HttpServletRequest request) {
		
		// return a mostly constant last modified date for all files passing
		// through the jsp (.withjstl) servlet
		// this allows the files to cache until we say so
		if (interpretJstl) {
			log.debug("returning last modified date of : {} for : {}", lastModified, request.getPathInfo());
			return lastModified;
		}
		
		// the spring servletdispatcher will try to get the lastModified date
		// from the actual file in this case
		return -1;
	}
	
	public static void setLastModified(Long lastModified) {
		PseudoStaticContentController.lastModified = lastModified;
	}
	
	@Override
	public void globalPropertyChanged(GlobalProperty newValue) {
		// reset for every global property change
		setLastModified(System.currentTimeMillis());
	}
	
	@Override
	public void globalPropertyDeleted(String propertyName) {
		// reset for every global property change
		setLastModified(System.currentTimeMillis());
	}
	
	@Override
	public boolean supportsPropertyName(String propertyName) {
		return true;
	}
	
	public static void invalidateCachedResources(Map<String, String> newValue) {
		setLastModified(System.currentTimeMillis());
	}
}
File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/StartupFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.CommonsLogLogChute;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.FactoryConfiguration;
import org.apache.velocity.tools.config.ToolConfiguration;
import org.apache.velocity.tools.config.ToolboxConfiguration;
import org.openmrs.OpenmrsCharacterEscapes;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.logging.MemoryAppender;
import org.openmrs.logging.OpenmrsLoggingUtil;
import org.openmrs.util.LocaleUtility;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.Listener;
import org.openmrs.web.WebConstants;
import org.openmrs.web.filter.initialization.InitializationFilter;
import org.openmrs.web.filter.update.UpdateFilter;
import org.openmrs.web.filter.util.FilterUtil;
import org.openmrs.web.filter.util.LocalizationTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class used when a small wizard is needed before Spring, jsp, etc has been started up.
 *
 * @see UpdateFilter
 * @see InitializationFilter
 */
public abstract class StartupFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(StartupFilter.class);
	
	protected static VelocityEngine velocityEngine = null;
	
	public static final String AUTO_RUN_OPENMRS = "auto_run_openmrs";
	
	/**
	 * Set by the {@link #init(FilterConfig)} method so that we have access to the current
	 * {@link ServletContext}
	 */
	protected FilterConfig filterConfig = null;
	
	/**
	 * Records errors that will be displayed to the user
	 */
	protected Map<String, Object[]> errors = new HashMap<>();
	
	/**
	 * Messages that will be displayed to the user
	 */
	protected Map<String, Object[]> msgs = new HashMap<>();
	
	/**
	 * Used for configuring tools within velocity toolbox
	 */
	private ToolContext toolContext = null;
	
	/**
	 * The web.xml file sets this {@link StartupFilter} to be the first filter for all requests.
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 *      javax.servlet.FilterChain)
	 */
	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		if (((HttpServletRequest)request).getServletPath().equals("/health/started")) {
			((HttpServletResponse) response).setStatus(Listener.isOpenmrsStarted() ? HttpServletResponse.SC_OK : HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		else if (skipFilter((HttpServletRequest) request)) {
			chain.doFilter(request, response);
		} else {
			
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			String servletPath = httpRequest.getServletPath();
			// for all /images and /initfilter/scripts files, write the path
			// (the "/initfilter" part is needed so that the openmrs_static_context-servlet.xml file doesn't
			//  get instantiated early, before the locale messages are all set up)
			if (servletPath.startsWith("/images") || servletPath.startsWith("/initfilter/scripts")) {
				// strip out the /initfilter part
				servletPath = servletPath.replaceFirst("/initfilter", "/WEB-INF/view");
				// writes the actual image file path to the response
				Path filePath = Paths.get(filterConfig.getServletContext().getRealPath(servletPath)).normalize();
				Path fullFilePath = filePath;
				if (httpRequest.getPathInfo() != null) {
					fullFilePath = fullFilePath.resolve(httpRequest.getPathInfo());
					if (!(fullFilePath.normalize().startsWith(filePath))) {
						log.warn("Detected attempted directory traversal in request for {}", httpRequest.getPathInfo());
						return;
					}
				}
				
				try (InputStream imageFileInputStream = new FileInputStream(fullFilePath.normalize().toFile())) {
					OpenmrsUtil.copyFile(imageFileInputStream, httpResponse.getOutputStream());
				}
				catch (FileNotFoundException e) {
					log.error("Unable to find file: {}", filePath, e);
				}
				catch (IOException e) {
					log.warn("An error occurred while handling file {}", filePath, e);
				}
			} else if (servletPath.startsWith("/scripts")) {
				log.error(
				    "Calling /scripts during the initializationfilter pages will cause the openmrs_static_context-servlet.xml to initialize too early and cause errors after startup.  Use '/initfilter"
				            + servletPath + "' instead.");
			}
			// for anything but /initialsetup
			else if (!httpRequest.getServletPath().equals("/" + WebConstants.SETUP_PAGE_URL)
			        && !httpRequest.getServletPath().equals("/" + AUTO_RUN_OPENMRS)) {
				// send the user to the setup page
				httpResponse.sendRedirect("/" + WebConstants.WEBAPP_NAME + "/" + WebConstants.SETUP_PAGE_URL);
			} else {
				
				if ("GET".equals(httpRequest.getMethod())) {
					doGet(httpRequest, httpResponse);
				} else if ("POST".equals(httpRequest.getMethod())) {
					// only clear errors before POSTS so that redirects can show errors too.
					errors.clear();
					msgs.clear();
					doPost(httpRequest, httpResponse);
				}
			}
			// Don't continue down the filter chain otherwise Spring complains
			// that it hasn't been set up yet.
			// The jsp and servlet filter are also on this chain, so writing to
			// the response directly here is the only option
		}
	}
	
	/**
	 * Convenience method to set up the velocity context properly
	 */
	private void initializeVelocity() {
		if (velocityEngine == null) {
			velocityEngine = new VelocityEngine();
			
			Properties props = new Properties();
			props.setProperty(RuntimeConstants.RUNTIME_LOG, "startup_wizard_vel.log");
			// Linux requires setting logging properties to initialize Velocity Context.
			props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
			    "org.apache.velocity.runtime.log.CommonsLogLogChute");
			props.setProperty(CommonsLogLogChute.LOGCHUTE_COMMONS_LOG_NAME, "initial_wizard_velocity");
			
			// so the vm pages can import the header/footer
			props.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
			props.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
			props.setProperty("class.resource.loader.class",
			    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			
			try {
				velocityEngine.init(props);
			}
			catch (Exception e) {
				log.error("velocity init failed, because: {}", e, e);
			}
		}
	}
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on GET requests
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	protected abstract void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	        throws IOException, ServletException;
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on POST requests
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	protected abstract void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	        throws IOException, ServletException;
	
	/**
	 * All private attributes on this class are returned to the template via the velocity context and
	 * reflection
	 *
	 * @param templateName the name of the velocity file to render. This name is prepended with
	 *            {@link #getTemplatePrefix()}
	 * @param referenceMap
	 * @param httpResponse
	 */
	protected void renderTemplate(String templateName, Map<String, Object> referenceMap, HttpServletResponse httpResponse)
	        throws IOException {
		// first we should get velocity tools context for current client request (within
		// his http session) and merge that tools context with basic velocity context
		if (referenceMap == null) {
			return;
		}
		
		Object locale = referenceMap.get(FilterUtil.LOCALE_ATTRIBUTE);
		ToolContext velocityToolContext = getToolContext(
		    locale != null ? locale.toString() : Context.getLocale().toString());
		VelocityContext velocityContext = new VelocityContext(velocityToolContext);
		
		for (Map.Entry<String, Object> entry : referenceMap.entrySet()) {
			velocityContext.put(entry.getKey(), entry.getValue());
		}
		
		Object model = getUpdateFilterModel();
		
		// put each of the private varibles into the template for convenience
		for (Field field : model.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				velocityContext.put(field.getName(), field.get(model));
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				log.error("Error generated while getting field value: " + field.getName(), e);
			}
		}
		
		String fullTemplatePath = getTemplatePrefix() + templateName;
		InputStream templateInputStream = getClass().getClassLoader().getResourceAsStream(fullTemplatePath);
		if (templateInputStream == null) {
			throw new IOException("Unable to find " + fullTemplatePath);
		}
		
		velocityContext.put("errors", errors);
		velocityContext.put("msgs", msgs);
		
		// explicitly set the content type for the response because some servlet containers are assuming text/plain
		httpResponse.setContentType("text/html");
		
		try {
			velocityEngine.evaluate(velocityContext, httpResponse.getWriter(), this.getClass().getName(),
			    new InputStreamReader(templateInputStream, StandardCharsets.UTF_8));
		}
		catch (Exception e) {
			throw new APIException("Unable to process template: " + fullTemplatePath, e);
		}
	}
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		initializeVelocity();
	}
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}
	
	/**
	 * This string is prepended to all templateNames passed to
	 * {@link #renderTemplate(String, Map, HttpServletResponse)}
	 *
	 * @return string to prepend as the path for the templates
	 */
	protected String getTemplatePrefix() {
		return "org/openmrs/web/filter/";
	}
	
	/**
	 * The model that is used as the backer for all pages in this startup wizard. Should never return
	 * null.
	 *
	 * @return the stored formbacking/model object
	 */
	protected abstract Object getUpdateFilterModel();
	
	/**
	 * If this returns true, this filter fails early and quickly. All logic is skipped and startup and
	 * usage continue normally.
	 *
	 * @return true if this filter can be skipped
	 */
	public abstract boolean skipFilter(HttpServletRequest request);

	/**
	 * Convenience method to read the last 5 log lines from the MemoryAppender
	 * 
	 * The log lines will be added to the "logLines" key
	 * 
	 * @param result A map to be returned as a JSON document
	 */
	protected void addLogLinesToResponse(Map<String, Object> result) {
		MemoryAppender appender = OpenmrsLoggingUtil.getMemoryAppender();
		if (appender != null) {
			List<String> logLines = appender.getLogLines();
			
			// truncate the list to the last five so we don't overwhelm jquery
			if (logLines.size() > 5) {
				logLines = logLines.subList(logLines.size() - 5, logLines.size());
			}
			
			result.put("logLines", logLines);
		} else {
			result.put("logLines", Collections.emptyList());
		}
	}
	
	/**
	 * Convenience method to convert the given object to a JSON string. Supports Maps, Lists, Strings,
	 * Boolean, Double
	 *
	 * @param object object to convert to json
	 * @return JSON string to be eval'd in javascript
	 */
	protected String toJSONString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getFactory().setCharacterEscapes(new OpenmrsCharacterEscapes());
		try {
			return mapper.writeValueAsString(object);
		}
		catch (IOException e) {
			log.error("Failed to convert object to JSON");
			throw new APIException(e);
		}
	}
	
	/**
	 * Gets tool context for specified locale parameter. If context does not exists, it creates new
	 * context, configured for that locale. Otherwise, it changes locale property of
	 * {@link LocalizationTool} object, that is being contained in tools context
	 *
	 * @param locale the string with locale parameter for configuring tools context
	 * @return the tool context object
	 */
	public ToolContext getToolContext(String locale) {
		Locale systemLocale = LocaleUtility.fromSpecification(locale);
		//Defaults to en if systemLocale is null or invalid e.g en_GBs
		if (systemLocale == null || !ArrayUtils.contains(Locale.getAvailableLocales(), systemLocale)) {
			systemLocale = Locale.ENGLISH;
		}
		// If tool context has not been configured yet
		if (toolContext == null) {
			// first we are creating manager for tools, factory for configuring tools 
			// and empty configuration object for velocity tool box
			ToolManager velocityToolManager = new ToolManager();
			FactoryConfiguration factoryConfig = new FactoryConfiguration();
			// since we are using one tool box for all request within wizard
			// we should propagate toolbox's scope on all application 
			ToolboxConfiguration toolbox = new ToolboxConfiguration();
			toolbox.setScope(Scope.APPLICATION);
			// next we are directly configuring custom localization tool by
			// setting its class name, locale property etc.
			ToolConfiguration localizationTool = new ToolConfiguration();
			localizationTool.setClassname(LocalizationTool.class.getName());
			localizationTool.setProperty(ToolContext.LOCALE_KEY, systemLocale);
			localizationTool.setProperty(LocalizationTool.BUNDLES_KEY, "messages");
			// and finally we are adding just configured tool into toolbox
			// and creating tool context for this toolbox
			toolbox.addTool(localizationTool);
			factoryConfig.addToolbox(toolbox);
			velocityToolManager.configure(factoryConfig);
			toolContext = velocityToolManager.createContext();
			toolContext.setUserCanOverwriteTools(true);
		} else {
			// if it already has been configured, we just pull out our custom localization tool 
			// from tool context, then changing its locale property and putting this tool back to the context
			// First, we need to obtain the value of default key annotation of our localization tool
			// class using reflection
			DefaultKey annotation = LocalizationTool.class.getAnnotation(DefaultKey.class);
			String key = annotation.value();
			//
			LocalizationTool localizationTool = (LocalizationTool) toolContext.get(key);
			localizationTool.setLocale(systemLocale);
			toolContext.put(key, localizationTool);
		}
		return toolContext;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/GZIPRequestWrapper.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Wraps Request for GZipFilter
 *
 */
public class GZIPRequestWrapper extends HttpServletRequestWrapper {
	
	protected ServletInputStream stream;
	
	public GZIPRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		stream = new GZIPRequestStream(request);
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		return stream;
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/GZIPResponseWrapper.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps Response for GZipFilter
 * 
 * @author Matt Raible, cmurphy@intechtual.com
 */
public class GZIPResponseWrapper extends HttpServletResponseWrapper {
	
	private static final Logger log = LoggerFactory.getLogger(GZIPResponseWrapper.class);
	
	protected HttpServletResponse origResponse;
	
	protected ServletOutputStream stream = null;
	
	protected PrintWriter writer = null;
	
	protected int error = 0;
	
	public GZIPResponseWrapper(HttpServletResponse response) {
		super(response);
		origResponse = response;
	}
	
	public ServletOutputStream createOutputStream() throws IOException {
		return new GZIPResponseStream(origResponse);
	}
	
	public void finishResponse() {
		try {
			if (writer != null) {
				writer.close();
			} else {
				if (stream != null && !((GZIPResponseStream)stream).closed()) {
					stream.close();
				}
			}
		}
		catch (IOException e) {
			log.error("Error during closing writer or stream", e);
		}
	}
	
	@Override
	public void flushBuffer() throws IOException {
		if (stream != null) {
			stream.flush();
		}
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called!");
		}
		
		if (stream == null) {
			stream = createOutputStream();
		}
		
		return stream;
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {
		// From cmurphy@intechtual.com to fix:
		// https://appfuse.dev.java.net/issues/show_bug.cgi?id=59
		if (this.origResponse != null && this.origResponse.isCommitted()) {
			return super.getWriter();
		}
		
		if (writer != null) {
			return writer;
		}
		
		if (stream != null) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}
		
		stream = createOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(stream, origResponse.getCharacterEncoding()));
		
		return writer;
	}
	
	/**
	 * @see javax.servlet.http.HttpServletResponse#sendError(int, java.lang.String)
	 */
	@Override
	public void sendError(int error, String message) throws IOException {
		super.sendError(error, message);
		this.error = error;
		
		log.debug("sending error: {} [{}]", error, message);
	}
	
	public void setContentLength(int length) {
		//Intentionally left blank to ignore whatever length the caller sets, because
		//we are going to zip the response and hence end up with a smaller length.
		//Without this empty method, the base class's setContentLength() method will be
		//called, leading to the browser's waiting for more data than what we actually
		//have for the compressed output, hence slowing down the response. TRUNK-5978
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/CookieClearingFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This servlet filter exists to remove session cookies when a user logs out.
 * <p/>
 * This filter is configurable at runtime using the following runtime properties:
 * <ul>
 *     <li><tt>cookieClearingFilter.toClear = comma separated list of cookies to clear</tt>
 *     determines the cookies we will try to clear. If unset, will default to just clearing the JSESSIONID cookie.</li>
 * </ul>
 */
public class CookieClearingFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(CookieClearingFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		
		// if an earlier filter has already written a response, we cannot do anything
		if (response.isCommitted()) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String[] cookiesToClear = new String[0];
		
		// the try-catch here is defensive; if, for whatever reason, we cannot parse this setting, this filter should not
		// stop the request
		try {
			Properties properties = Context.getRuntimeProperties();
			String cookiesToClearSetting = properties.getProperty("cookieClearingFilter.toClear", "JSESSIONID");
			
			if (StringUtils.isNotBlank(cookiesToClearSetting)) {
				cookiesToClear = Arrays.stream(cookiesToClearSetting.split("\\s*,\\s*")).map(String::trim).toArray(
					String[]::new);
			}
		}
		catch (Exception e) {
			log.warn("Caught exception while trying to determine cookies to clear", e);
		}
		
		boolean requestHasSession = false;
		if (cookiesToClear.length > 0) {
			// we need to track whether this request initially was part of a session
			// if it was and there is no valid request at the end of the session, we clear the session cookies
			requestHasSession = request.getRequestedSessionId() != null;
		}
		
		// handle the request
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			if (cookiesToClear.length > 0 && !response.isCommitted()) {
				HttpSession session = request.getSession(false);
				// session was invalidated
				if (session == null && requestHasSession) {
					for (Cookie cookie : request.getCookies()) {
						for (String cookieToClear : cookiesToClear) {
							if (cookieToClear.equalsIgnoreCase(cookie.getName())) {
								Cookie clearedCookie = new Cookie(cookie.getName(), null);
								String contextPath = request.getContextPath();
								clearedCookie.setPath(
									contextPath == null || contextPath.trim().equals("") ? "/" : contextPath);
								clearedCookie.setMaxAge(0);
								clearedCookie.setHttpOnly(true);
								clearedCookie.setSecure(request.isSecure());
								response.addCookie(clearedCookie);
								break;
							}
						}
					}
				}
			}
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/JspClassLoaderFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.openmrs.util.OpenmrsClassLoader;

/**
 * Simple filter class to set the OpenMRS class loader as the context class loader of the current
 * thread so that JSPs can use EL functions defined in modules
 */
public class JspClassLoaderFilter implements Filter {
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	        ServletException {
		// Set thread's class loader
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		
		// Carry on up the chain
		chain.doFilter(request, response);
	}
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/GZIPRequestStream.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * Wraps Request Stream for GZipFilter
 *
 */
public class GZIPRequestStream extends ServletInputStream {
	
	//reference to the gzipped input stream
	protected GZIPInputStream zipInput;
	
	public GZIPRequestStream(HttpServletRequest request) throws IOException {
		super();
		this.zipInput = new GZIPInputStream(request.getInputStream());
	}
	
	@Override
	public int read(byte[] buf, int off, int len) throws IOException {
		return zipInput.read(buf, off, len);
	}
	
	@Override
	public int read() throws IOException {
		return zipInput.read();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return zipInput.read(b);
	}

	@Override
	public boolean isFinished() {
		throw new UnsupportedOperationException("Asynchonous operation is not supported.");
	}

	@Override
	public boolean isReady() {
		throw new UnsupportedOperationException("Asynchonous operation is not supported.");
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		throw new UnsupportedOperationException("Asynchonous operation is not supported.");
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/OpenmrsFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.web.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This is the custom OpenMRS filter. It is defined as the filter of choice in the web.xml file. All
 * page/object calls run through the doFilter method so we can wrap every session with the user's
 * userContext (which holds the user's authenticated info). This is needed because the OpenMRS API
 * keeps authentication information on the current Thread. Web applications use a different thread
 * per request, so before each request this filter will make sure that the UserContext (the
 * authentication information) is on the Thread.
 */
public class OpenmrsFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(OpenmrsFilter.class);
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		log.debug("Destroying filter");
	}
	
	/**
	 * This method is called for every request for a page/image/javascript file/etc The main point
	 * of this is to make sure the user's current userContext is on the session and on the current
	 * thread
	 *
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain)
	        throws ServletException, IOException {
		
		HttpSession httpSession = httpRequest.getSession();
		
		// used by htmlInclude tag
		httpRequest.setAttribute(WebConstants.INIT_REQ_UNIQUE_ID, String.valueOf(System.currentTimeMillis()));
		
		log.debug("requestURI {}", httpRequest.getRequestURI());
		log.debug("requestURL {}", httpRequest.getRequestURL());
		log.debug("request path info {}", httpRequest.getPathInfo());
		
		// User context is created if it doesn't already exist and added to the session
		// note: this usercontext storage logic is copied to webinf/view/uncaughtexception.jsp to 
		// 		 prevent stack traces being shown to non-authenticated users
		UserContext userContext = (UserContext) httpSession.getAttribute(WebConstants.OPENMRS_USER_CONTEXT_HTTPSESSION_ATTR);
		
		// default the session username attribute to anonymous
		httpSession.setAttribute("username", "-anonymous user-");
		
		// if there isn't a userContext on the session yet, create one
		// and set it onto the session
		if (userContext == null) {
			userContext = new UserContext(Context.getAuthenticationScheme());
			httpSession.setAttribute(WebConstants.OPENMRS_USER_CONTEXT_HTTPSESSION_ATTR, userContext);
			
			log.debug("Just set user context {} as attribute on session", userContext);
		} else {
			// set username as attribute on session so parent servlet container 
			// can identify sessions easier
			User user = userContext.getAuthenticatedUser();
			if (user != null) {
				httpSession.setAttribute("username", user.getUsername());
			}
		}
		
		// set the locale on the session (for the servlet container as well)
		httpSession.setAttribute("locale", userContext.getLocale());
		
		//TODO We do not cache the csrfguard javascript file because it contains the
		//csrf token that is dynamically embedded in forms. For this to work,
		//the OpenmrsFilter should be before the CSRFGuard filter in web.xml
		if (httpRequest.getRequestURI().endsWith("csrfguard")) {
			httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
			httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0.
			httpResponse.setHeader("Expires", "0"); // Proxies.
		}
		
		// Add the user context to the current thread 
		Context.setUserContext(userContext);
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		
		log.debug("before chain.Filter");
		
		// continue the filter chain (going on to spring, authorization, etc)
		try {
			chain.doFilter(httpRequest, httpResponse);
		}
		finally {
			Context.clearUserContext();
		}
		
		log.debug("after chain.doFilter");
		
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/GZIPResponseStream.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

/**
 * Wraps Response Stream for GZipFilter
 * 
 * @author Matt Raible
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:17:00 $
 */
public class GZIPResponseStream extends ServletOutputStream {
	
	// abstraction of the output stream used for compression
	protected OutputStream bufferedOutput;
	
	// state keeping variable for if close() has been called
	protected boolean closed;
	
	// reference to original response
	protected HttpServletResponse response;
	
	// reference to the output stream to the client's browser
	protected ServletOutputStream output;
	
	// default size of the in-memory buffer
	private int bufferSize = 50000;
	
	public GZIPResponseStream(HttpServletResponse response) throws IOException {
		super();
		closed = false;
		this.response = response;
		this.output = response.getOutputStream();
		bufferedOutput = new ByteArrayOutputStream();
	}
	
	@Override
	public void close() throws IOException {
		// verify the stream is yet to be closed
		if (closed) {
			throw new IOException("This output stream has already been closed");
		}
		
		// if we buffered everything in memory, gzip it
		if (bufferedOutput instanceof ByteArrayOutputStream) {
			// get the content
			ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;
			
			// prepare a gzip stream
			ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
			GZIPOutputStream gzipstream = new GZIPOutputStream(compressedContent);
			byte[] bytes = baos.toByteArray();
			gzipstream.write(bytes);
			gzipstream.finish();
			
			// get the compressed content
			byte[] compressedBytes = compressedContent.toByteArray();
			
			// set appropriate HTTP headers
			response.setContentLength(compressedBytes.length);
			response.addHeader("Content-Encoding", "gzip");
			output.write(compressedBytes);
			output.flush();
			output.close();
			closed = true;
		}
		// if things were not buffered in memory, finish the GZIP stream and response
		else if (bufferedOutput instanceof GZIPOutputStream) {
			// cast to appropriate type
			GZIPOutputStream gzipstream = (GZIPOutputStream) bufferedOutput;
			
			// finish the compression
			gzipstream.finish();
			
			// finish the response
			output.flush();
			output.close();
			closed = true;
		}
	}
	
	@Override
	public void flush() throws IOException {
		if (closed) {
			throw new IOException("Cannot flush a closed output stream");
		}
		
		bufferedOutput.flush();
	}
	
	@Override
	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		
		// make sure we aren't over the buffer's limit
		checkBufferSize(1);
		
		// write the byte to the temporary output
		bufferedOutput.write((byte) b);
	}
	
	private void checkBufferSize(int length) throws IOException {
		// check if we are buffering too large of a file
		if (bufferedOutput instanceof ByteArrayOutputStream) {
			ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;
			
			if ((baos.size() + length) > bufferSize) {
				// files too large to keep in memory are sent to the client without Content-Length specified
				response.addHeader("Content-Encoding", "gzip");
				
				// get existing bytes
				byte[] bytes = baos.toByteArray();
				
				// make new gzip stream using the response output stream
				GZIPOutputStream gzipstream = new GZIPOutputStream(output);
				gzipstream.write(bytes);
				
				// we are no longer buffering, send content via gzipstream
				bufferedOutput = gzipstream;
			}
		}
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		
		// make sure we aren't over the buffer's limit
		checkBufferSize(len);
		
		// write the content to the buffer
		bufferedOutput.write(b, off, len);
	}
	
	public boolean closed() {
		return this.closed;
	}
	
	public void reset() {
		//noop
	}

	@Override
	public boolean isReady() {
		throw new UnsupportedOperationException("Asynchonous operation is not supported.");
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		throw new UnsupportedOperationException("Asynchonous operation is not supported.");
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/GZIPFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter that compresses output with gzip (assuming that browser supports gzip). Code from <a
 * href="http://www.onjava.com/pub/a/onjava/2003/11/19/filters.html">
 * http://www.onjava.com/pub/a/onjava/2003/11/19/filters.html</a>. &copy; 2003 Jayson Falkner You
 * may freely use the code both commercially and non-commercially.
 */
public class GZIPFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(GZIPFilter.class);
	
	private Boolean cachedGZipEnabledFlag = null;
	
	private String cachedGZipCompressedRequestForPathAccepted = null;
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		try {
			request = performGZIPRequest(request);
		}
		catch (APIException e) {
			response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			return;
		}
		if (isGZIPSupported(request) && isGZIPEnabled()) {
			log.debug("GZIP supported and enabled, compressing response");
			
			GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
			
			chain.doFilter(request, wrappedResponse);
			wrappedResponse.finishResponse();
			
			return;
		}
		
		chain.doFilter(request, response);
	}
	
	/**
	 * Supports GZIP requests
	 * @param req request
	 * @return gzipped request
	 */
	public HttpServletRequest performGZIPRequest(HttpServletRequest req) {
		String contentEncoding = req.getHeader("Content-encoding");
		if (contentEncoding != null && contentEncoding.contains("gzip")) {
			if (!isCompressedRequestForPathAccepted(req.getRequestURI())) {
				throw new APIException("Unsupported Media Type");
			}
			
			log.debug("GZIP request supported");
			
			try {
				GZIPRequestWrapper wrapperRequest = new GZIPRequestWrapper(req);
				log.debug("GZIP request wrapped successfully");
				return wrapperRequest;
			}
			catch (IOException e) {
				log.error("Error during wrapping GZIP request " + e);
				return req;
			}
		} else {
			return req;
		}
		
	}
	
	/**
	 * Convenience method to test for GZIP capabilities
	 *
	 * @param req The current user request
	 * @return boolean indicating GZIP support
	 */
	private boolean isGZIPSupported(HttpServletRequest req) {
		String browserEncodings = req.getHeader("accept-encoding");
		boolean supported = ((browserEncodings != null) && (browserEncodings.contains("gzip")));
		
		String userAgent = req.getHeader("user-agent");
		
		if ((userAgent != null) && userAgent.startsWith("httpunit")) {
			log.debug("httpunit detected, disabling filter...");
			
			return false;
		} else {
			return supported;
		}
	}
	
	/**
	 * Returns global property gzip.enabled as boolean
	 */
	private boolean isGZIPEnabled() {
		if (cachedGZipEnabledFlag != null) {
			return cachedGZipEnabledFlag;
		}
		
		try {
			String gzipEnabled = Context.getAdministrationService().getGlobalProperty(
			    OpenmrsConstants.GLOBAL_PROPERTY_GZIP_ENABLED, "");

			cachedGZipEnabledFlag = Boolean.valueOf(gzipEnabled);
			return cachedGZipEnabledFlag;
		}
		catch (Exception e) {
			log.warn("Unable to get the global property: " + OpenmrsConstants.GLOBAL_PROPERTY_GZIP_ENABLED, e);
			// not caching the enabled flag here in case it becomes available
			// before the next request
			
			return false;
		}
	}
	
	/**
	 * Returns true if path matches pattern in gzip.acceptCompressedRequestsForPaths property
	 */
	private boolean isCompressedRequestForPathAccepted(String path) {
		try {
			if (cachedGZipCompressedRequestForPathAccepted == null) {
				cachedGZipCompressedRequestForPathAccepted = Context.getAdministrationService().getGlobalProperty(
				    OpenmrsConstants.GLOBAL_PROPERTY_GZIP_ACCEPT_COMPRESSED_REQUESTS_FOR_PATHS, "");
			}
			
			for (String acceptPath : cachedGZipCompressedRequestForPathAccepted.split(",")) {
				if (path.matches(acceptPath)) {
					return true;
				}
			}
			
			return false;
		}
		catch (Exception e) {
			log.warn("Unable to process the global property: "
			        + OpenmrsConstants.GLOBAL_PROPERTY_GZIP_ACCEPT_COMPRESSED_REQUESTS_FOR_PATHS, e);
			return false;
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/update/DatabaseUpdaterWrapper.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.update;

import java.util.List;
import org.openmrs.liquibase.LiquibaseProvider;
import org.openmrs.util.DatabaseUpdater;

public class DatabaseUpdaterWrapper {
	public List<DatabaseUpdater.OpenMRSChangeSet> getUnrunDatabaseChanges( LiquibaseProvider liquibaseProvider) throws Exception {
		return DatabaseUpdater.getUnrunDatabaseChanges( liquibaseProvider );
	}
	
	public boolean isLocked() {
		return DatabaseUpdater.isLocked();
	}

	public boolean updatesRequired() throws Exception {
		return DatabaseUpdater.updatesRequired();
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/update/UpdateFilterModel.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.update;

import java.util.List;

import org.openmrs.liquibase.LiquibaseProvider;
import org.openmrs.util.DatabaseUpdater.OpenMRSChangeSet;
import org.openmrs.util.DatabaseUpdaterLiquibaseProvider;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.RoleConstants;
import org.openmrs.web.WebConstants;
import org.openmrs.web.filter.StartupFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UpdateFilter} uses this model object to hold all properties that are edited by the
 * user in the wizard. All attributes on this model object are added to all templates rendered by
 * the {@link StartupFilter}.
 */
public class UpdateFilterModel {
	
	private static final Logger log = LoggerFactory.getLogger(UpdateFilterModel.class);
	
	// automatically given to the .vm files and used there
	public static final String HEADER_TEMPLATE = "org/openmrs/web/filter/update/header.vm";

	// automatically given to the .vm files and used there
	public static final String FOOTER_TEMPLATE = "org/openmrs/web/filter/update/footer.vm";
		
	public List<OpenMRSChangeSet> changes = null;
	
	public String superuserrole = RoleConstants.SUPERUSER;

	public String setupPageUrl = WebConstants.SETUP_PAGE_URL;
	
	public static final String OPENMRS_VERSION = OpenmrsConstants.OPENMRS_VERSION_SHORT;
	
	public Boolean updateRequired = false;
	
	private LiquibaseProvider liquibaseProvider;
	
	private DatabaseUpdaterWrapper databaseUpdaterWrapper;
	
	/**
	 * Default constructor that sets up some of the properties
	 */
	public UpdateFilterModel() {
		this(new DatabaseUpdaterLiquibaseProvider(), new DatabaseUpdaterWrapper());
		log.debug("executing default constructor...");
	}
	
	/**
	 * Constructor that allows to inject a Liquibase provider.
	 * 
	 * @param liquibaseProvider a Liquibase provider
	 */
	public UpdateFilterModel(LiquibaseProvider liquibaseProvider, DatabaseUpdaterWrapper databaseUpdaterWrapper) {
		log.debug("executing non-default constructor...");
		this.liquibaseProvider = liquibaseProvider;
		this.databaseUpdaterWrapper = databaseUpdaterWrapper;
		
		updateChanges();
		
		try {
			if (changes != null && !changes.isEmpty()) {
				updateRequired = true;
			} else {
				updateRequired = databaseUpdaterWrapper.updatesRequired();
			}
		}
		catch (Exception e) {
			// do nothing
		}
	}
	
	/**
	 * Convenience method that reads from liquibase again to get the most recent list of changesets that
	 * still need to be run.
	 */
	public void updateChanges() {
		log.debug("executing updateChanges()...");
		try {
			changes = databaseUpdaterWrapper.getUnrunDatabaseChanges(liquibaseProvider);
			
			// not sure why this is necessary...
			if (changes == null && databaseUpdaterWrapper.isLocked()) {
				changes = databaseUpdaterWrapper.getUnrunDatabaseChanges(liquibaseProvider);
			}
		}
		catch (Exception e) {
			log.error("Unable to get the database changes", e);
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/update/UpdateFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.update;

import liquibase.changelog.ChangeSet;
import liquibase.exception.LockException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.liquibase.ChangeLogDetective;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.DatabaseUpdater;
import org.openmrs.liquibase.ChangeSetExecutorCallback;
import org.openmrs.util.DatabaseUpdaterLiquibaseProvider;
import org.openmrs.util.InputRequiredException;
import org.openmrs.liquibase.ChangeLogVersionFinder;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.RoleConstants;
import org.openmrs.util.Security;
import org.openmrs.web.Listener;
import org.openmrs.web.WebDaemon;
import org.openmrs.web.filter.StartupFilter;
import org.openmrs.web.filter.initialization.InitializationFilter;
import org.openmrs.web.filter.util.CustomResourceLoader;
import org.openmrs.web.filter.util.ErrorMessageConstants;
import org.openmrs.web.filter.util.FilterUtil;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This is the second filter that is processed. It is only active when OpenMRS has some liquibase
 * updates that need to be run. If updates are needed, this filter/wizard asks for a super user to
 * authenticate and review the updates before continuing.
 */
public class UpdateFilter extends StartupFilter {
	
	protected final org.slf4j.Logger log = LoggerFactory.getLogger(UpdateFilter.class);
	
	/**
	 * The velocity macro page to redirect to if an error occurs or on initial startup
	 */
	private static final String DEFAULT_PAGE = "maintenance.vm";
	
	/**
	 * The page that lists off all the currently unexecuted changes
	 */
	private static final String REVIEW_CHANGES = "reviewchanges.vm";
	
	private static final String PROGRESS_VM_AJAXREQUEST = "updateProgress.vm.ajaxRequest";
	
	/**
	 * The model object behind this set of screens
	 */
	private UpdateFilterModel updateFilterModel = null;
	
	/**
	 * Variable set as soon as the update is done or verified to not be needed so that future calls
	 * through this filter are a simple boolean check
	 */
	private static boolean updatesRequired = true;
	
	/**
	 * Used on all pages after the first to make sure the user isn't trying to cheat and do some url
	 * magic to hack in.
	 */
	private boolean authenticatedSuccessfully = false;
	
	private UpdateFilterCompletion updateJob;
	
	/**
	 * Variable set to true as soon as the update begins and set to false when the process ends. This
	 * thread should only be accesses through the synchronized method.
	 */
	private static boolean isDatabaseUpdateInProgress = false;
	
	/**
	 * Variable set to true when the db lock is released. It's needed to prevent repeatedly releasing
	 * this lock by other threads. This var should only be accessed through the synchronized method.
	 */
	private static Boolean lockReleased = false;
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on GET requests
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	        throws IOException, ServletException {
		
		Map<String, Object> referenceMap = new HashMap<>();
		checkLocaleAttributesForFirstTime(httpRequest);
		// we need to save current user language in references map since it will be used when template
		// will be rendered
		if (httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE) != null) {
			referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE,
			    httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
		}
		// do step one of the wizard
		renderTemplate(DEFAULT_PAGE, referenceMap, httpResponse);
	}
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on POST requests
	 *
	 * @see org.openmrs.web.filter.StartupFilter#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected synchronized void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	        throws IOException, ServletException {
		
		final String updJobStatus = "updateJobStarted";
		String page = httpRequest.getParameter("page");
		Map<String, Object> referenceMap = new HashMap<>();
		if (httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE) != null) {
			referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE,
			    httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
		}
		
		// step one
		if (DEFAULT_PAGE.equals(page)) {
			
			String username = httpRequest.getParameter("username");
			String password = httpRequest.getParameter("password");
			
			log.debug("Attempting to authenticate user: " + username);
			if (authenticateAsSuperUser(username, password)) {
				log.debug("Authentication successful.  Redirecting to 'reviewupdates' page.");
				// set a variable so we know that the user started here
				authenticatedSuccessfully = true;
				
				//Set variable to tell us whether updates are already in progress
				referenceMap.put("isDatabaseUpdateInProgress", isDatabaseUpdateInProgress);
				
				// if another super user has already launched database update
				// allow current super user to review update progress
				if (isDatabaseUpdateInProgress) {
					referenceMap.put(updJobStatus, true);
					httpResponse.setContentType("text/html");
					renderTemplate(REVIEW_CHANGES, referenceMap, httpResponse);
					return;
				}
				
				// we will only get here if the db update is NOT running. 
				// so if we find a db lock, we should release it because
				// it was leftover from a previous db update crash
				
				if (!isLockReleased() && DatabaseUpdater.isLocked()) {
					// first we trying to release db lock if it exists
					try {
						DatabaseUpdater.releaseDatabaseLock();
						setLockReleased(true);
					}
					catch (LockException e) {
						// do nothing
					}
					// if lock was released successfully we need to get unrun changes
					updateFilterModel.updateChanges();
				}
				
				// need to configure velocity tool box for using user's preferred locale
				// so we should store it for further using when configuring velocity tool context
				String localeParameter = FilterUtil.restoreLocale(username);
				httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, localeParameter);
				referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE, localeParameter);
				
				renderTemplate(REVIEW_CHANGES, referenceMap, httpResponse);
			} else {
				// if not authenticated, show main page again
				try {
					log.debug("Sleeping for 3 seconds because of a bad username/password");
					Thread.sleep(3000);
				}
				catch (InterruptedException e) {
					log.error("Unable to sleep", e);
					throw new ServletException("Got interrupted while trying to sleep thread", e);
				}
				errors.put(ErrorMessageConstants.UPDATE_ERROR_UNABLE_AUTHENTICATE, null);
				renderTemplate(DEFAULT_PAGE, referenceMap, httpResponse);
			}
		}
		// step two of wizard in case if there were some warnings
		else if (REVIEW_CHANGES.equals(page)) {
			
			if (!authenticatedSuccessfully) {
				// throw the user back to the main page because they are cheating
				renderTemplate(DEFAULT_PAGE, referenceMap, httpResponse);
				return;
			}
			
			//if no one has run any required updates
			if (!isDatabaseUpdateInProgress) {
				isDatabaseUpdateInProgress = true;
				updateJob = new UpdateFilterCompletion();
				updateJob.start();
				
				// allows current user see progress of running update
				// and also will hide the "Run Updates" button
				
				referenceMap.put(updJobStatus, true);
			} else {
				referenceMap.put("isDatabaseUpdateInProgress", true);
				// as well we need to allow current user to
				// see progress of already started updates
				// and also will hide the "Run Updates" button
				referenceMap.put(updJobStatus, true);
			}
			
			renderTemplate(REVIEW_CHANGES, referenceMap, httpResponse);
			
		} else if (PROGRESS_VM_AJAXREQUEST.equals(page)) {
			
			httpResponse.setContentType("text/json");
			httpResponse.setHeader("Cache-Control", "no-cache");
			Map<String, Object> result = new HashMap<>();
			if (updateJob != null) {
				result.put("hasErrors", updateJob.hasErrors());
				if (updateJob.hasErrors()) {
					errors.putAll(updateJob.getErrors());
				}
				
				if (updateJob.hasWarnings() && updateJob.getExecutingChangesetId() == null) {
					result.put("hasWarnings", updateJob.hasWarnings());
					StringBuilder sb = new StringBuilder("<ul>");
					
					for (String warning : updateJob.getUpdateWarnings()) {
						sb.append("<li>").append(warning).append("</li>");
					}
					
					sb.append("</ul>");
					result.put("updateWarnings", sb.toString());
					result.put("updateLogFile",
					    StringUtils.replace(
					        OpenmrsUtil.getApplicationDataDirectory() + DatabaseUpdater.DATABASE_UPDATES_LOG_FILE, "\\",
					        "\\\\"));
					updateJob.hasUpdateWarnings = false;
					updateJob.getUpdateWarnings().clear();
				}
				
				result.put("updatesRequired", updatesRequired());
				result.put("message", updateJob.getMessage());
				result.put("changesetIds", updateJob.getChangesetIds());
				result.put("executingChangesetId", updateJob.getExecutingChangesetId());
				
				addLogLinesToResponse(result);
			}
			
			String jsonText = toJSONString(result);
			httpResponse.getWriter().write(jsonText);
		}
	}
	
	/**
	 * It sets locale attribute for current session when user is making first GET http request to
	 * application. It retrieves user locale from request object and checks if this locale is supported
	 * by application. If not, it tries to load system default locale. If it's not specified it uses
	 * {@link Locale#ENGLISH} by default
	 *
	 * @param httpRequest the http request object
	 */
	public void checkLocaleAttributesForFirstTime(HttpServletRequest httpRequest) {
		Locale locale = httpRequest.getLocale();
		String systemDefaultLocale = FilterUtil.readSystemDefaultLocale(null);
		if (CustomResourceLoader.getInstance(httpRequest).getAvailablelocales().contains(locale)) {
			httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, locale.toString());
			log.info("Used client's locale " + locale.toString());
		} else if (StringUtils.isNotBlank(systemDefaultLocale)) {
			httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, systemDefaultLocale);
			log.info("Used system default locale " + systemDefaultLocale);
		} else {
			httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, Locale.ENGLISH.toString());
			log.info("Used default locale " + Locale.ENGLISH.toString());
		}
	}
	
	/**
	 * Look in the users table for a user with this username and password and see if they have a role of
	 * {@link RoleConstants#SUPERUSER}.
	 *
	 * @param usernameOrSystemId user entered username
	 * @param password user entered password
	 * @return true if this user has the super user role
	 * @see #isSuperUser(Connection, Integer) <strong>Should</strong> return false if given invalid
	 *      credentials <strong>Should</strong> return false if given user is not superuser
	 *      <strong>Should</strong> return true if given user is superuser <strong>Should</strong> not
	 *      authorize retired superusers <strong>Should</strong> authenticate with systemId
	 */
	protected boolean authenticateAsSuperUser(String usernameOrSystemId, String password) throws ServletException {
		Connection connection = null;
		try {
			connection = DatabaseUpdater.getConnection();
			
			String select = "select user_id, password, salt from users where (username = ? or system_id = ?) and retired = '0'";
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(select);
				statement.setString(1, usernameOrSystemId);
				statement.setString(2, usernameOrSystemId);
				
				if (statement.execute()) {
					ResultSet results = null;
					try {
						results = statement.getResultSet();
						if (results.next()) {
							Integer userId = results.getInt(1);
							DatabaseUpdater.setAuthenticatedUserId(userId);
							String storedPassword = results.getString(2);
							String salt = results.getString(3);
							String passwordToHash = password + salt;
							return Security.hashMatches(storedPassword, passwordToHash) && isSuperUser(connection, userId);
						}
					}
					finally {
						if (results != null) {
							try {
								results.close();
							}
							catch (Exception resultsCloseEx) {
								log.error("Failed to quietly close ResultSet", resultsCloseEx);
							}
						}
					}
				}
			}
			finally {
				if (statement != null) {
					try {
						statement.close();
					}
					catch (Exception statementCloseEx) {
						log.error("Failed to quietly close Statement", statementCloseEx);
					}
				}
			}
		}
		catch (Exception connectionEx) {
			log.error(
			    "Error while trying to authenticate as super user. Ignore this if you are upgrading from OpenMRS 1.5 to 1.6",
			    connectionEx);
			
			// we may not have upgraded User to have retired instead of voided yet, so if the query above fails, we try
			// again the old way
			if (connection != null) {
				String select = "select user_id, password, salt from users where (username = ? or system_id = ?) and voided = '0'";
				PreparedStatement statement = null;
				try {
					statement = connection.prepareStatement(select);
					statement.setString(1, usernameOrSystemId);
					statement.setString(2, usernameOrSystemId);
					if (statement.execute()) {
						ResultSet results = null;
						try {
							results = statement.getResultSet();
							if (results.next()) {
								Integer userId = results.getInt(1);
								DatabaseUpdater.setAuthenticatedUserId(userId);
								String storedPassword = results.getString(2);
								String salt = results.getString(3);
								String passwordToHash = password + salt;
								return Security.hashMatches(storedPassword, passwordToHash)
								        && isSuperUser(connection, userId);
							}
						}
						finally {
							if (results != null) {
								try {
									results.close();
								}
								catch (Exception resultsCloseEx) {
									log.error("Failed to quietly close ResultSet", resultsCloseEx);
								}
							}
						}
					}
				}
				catch (Exception unhandeledEx) {
					log.error("Error while trying to authenticate as super user (voided version)", unhandeledEx);
				}
				finally {
					if (statement != null) {
						try {
							statement.close();
						}
						catch (Exception statementCloseEx) {
							log.error("Failed to quietly close Statement", statementCloseEx);
						}
					}
				}
			}
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				}
				catch (SQLException e) {
					log.debug("Error while closing the database", e);
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Checks the given user to see if they have been given the {@link RoleConstants#SUPERUSER}
	 * role. This method does not look at child roles.
	 *
	 * @param connection the java sql connection to use
	 * @param userId the user id to look at
	 * @return true if the given user is a super user
	 * @throws SQLException <strong>Should</strong> return true if given user has superuser role
	 *             <strong>Should</strong> return false if given user does not have the super user role
	 */
	protected boolean isSuperUser(Connection connection, Integer userId) throws SQLException {
		// the 'Administrator' part of this string is necessary because if the database was upgraded
		// by OpenMRS 1.6 alpha then System Developer was renamed to that. This has to be here so we
		// can roll back that change in 1.6 beta+
		String select = "select 1 from user_role where user_id = ? and (role = ? or role = 'Administrator')";
		PreparedStatement statement = connection.prepareStatement(select);
		statement.setInt(1, userId);
		statement.setString(2, RoleConstants.SUPERUSER);
		if (statement.execute()) {
			ResultSet results = statement.getResultSet();
			if (results.next()) {
				return results.getInt(1) == 1;
			}
		}
		
		return false;
	}
	
	/**
	 * `` Do everything to get openmrs going.
	 *
	 * @param servletContext the servletContext from the filterconfig
	 * @see Listener#startOpenmrs(ServletContext)
	 */
	private void startOpenmrs(ServletContext servletContext) throws Exception {
		// start spring
		// after this point, all errors need to also call: contextLoader.closeWebApplicationContext(event.getServletContext())
		// logic copied from org.springframework.web.context.ContextLoaderListener
		ContextLoader contextLoader = new ContextLoader();
		contextLoader.initWebApplicationContext(servletContext);
		
		try {
			WebDaemon.startOpenmrs(servletContext);
		}
		catch (Exception exception) {
			contextLoader.closeWebApplicationContext(servletContext);
			throw exception;
		}
	}
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		
		log.debug("Initializing the UpdateFilter");
		
		if (!InitializationFilter.initializationRequired()
		        || (Listener.isSetupNeeded() && Listener.runtimePropertiesFound())) {
			updateFilterModel = new UpdateFilterModel();
			/*
			 * In this case, Listener#runtimePropertiesFound == true and InitializationFilter Wizard is skipped,
			 * so no need to reset Context's RuntimeProperties again, because of Listener.contextInitialized has set it.
			 */
			try {
				// this pings the DatabaseUpdater.updatesRequired which also
				// considers a db lock to be a 'required update'
				if (updateFilterModel.updateRequired) {
					setUpdatesRequired(true);
				} else if (updateFilterModel.changes == null) {
					setUpdatesRequired(false);
				} else {
					log.debug("Setting updates required to {} because of the size of unrun changes", (!updateFilterModel.changes.isEmpty()));
					setUpdatesRequired(!updateFilterModel.changes.isEmpty());
				}
			}
			catch (Exception e) {
				throw new ServletException("Unable to determine if updates are required", e);
			}
		} else {
			/*
			 * The initialization wizard will update the database to the latest version, so the user will not need any updates here.
			 * See end of InitializationFilter#InitializationCompletion
			 */
			log.debug(
			    "Setting updates required to false because the user doesn't have any runtime properties yet or database is empty");
			setUpdatesRequired(false);
		}
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#getUpdateFilterModel()
	 */
	@Override
	protected Object getUpdateFilterModel() {
		// this object was initialized in the #init(FilterConfig) method
		return updateFilterModel;
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#skipFilter(HttpServletRequest)
	 */
	@Override
	public boolean skipFilter(HttpServletRequest httpRequest) {
		return !PROGRESS_VM_AJAXREQUEST.equals(httpRequest.getParameter("page")) && !updatesRequired();
	}
	
	/**
	 * Used by the Listener to know if this filter wants to do its magic
	 *
	 * @return true if updates have been determined to be required
	 * @see #init(FilterConfig)
	 * @see Listener#isSetupNeeded()
	 * @see Listener#contextInitialized(ServletContextEvent)
	 */
	public static synchronized boolean updatesRequired() {
		return updatesRequired;
	}
	
	/**
	 * @param updatesRequired the updatesRequired to set
	 */
	public static synchronized void setUpdatesRequired(boolean updatesRequired) {
		UpdateFilter.updatesRequired = updatesRequired;
	}
	
	/**
	 * Indicates if database lock was released. It will also used to prevent releasing existing lock of
	 * liquibasechangeloglock table by another user, when he also tries to run database update when
	 * another user is currently running it
	 */
	public static Boolean isLockReleased() {
		return lockReleased;
	}
	
	public static synchronized void setLockReleased(Boolean lockReleased) {
		UpdateFilter.lockReleased = lockReleased;
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#getTemplatePrefix()
	 */
	@Override
	protected String getTemplatePrefix() {
		return "org/openmrs/web/filter/update/";
	}
	
	/**
	 * This class controls the final steps and is used by the ajax calls to know what updates have been
	 * executed. TODO: Break this out into a separate (non-inner) class
	 */
	private class UpdateFilterCompletion {
		
		private Thread thread;
		
		private String executingChangesetId = null;
		
		private List<String> changesetIds = new ArrayList<>();
		
		private Map<String, Object[]> errors = new HashMap<>();
		
		private String message = null;
		
		private boolean erroneous = false;
		
		private boolean hasUpdateWarnings = false;
		
		private List<String> updateWarnings = new LinkedList<>();
		
		public synchronized void reportError(String error, Object... params) {
			Map<String, Object[]> reportedErrors = new HashMap<>();
			reportedErrors.put(error, params);
			reportErrors(reportedErrors);
		}
		
		public synchronized void reportErrors(Map<String, Object[]> errs) {
			errors.putAll(errs);
			erroneous = true;
		}
		
		public synchronized boolean hasErrors() {
			return erroneous;
		}
		
		public synchronized Map<String, Object[]> getErrors() {
			return errors;
		}
		
		/**
		 * Start the completion stage. This fires up the thread to do all the work.
		 */
		public void start() {
			setUpdatesRequired(true);
			thread.start();
		}
		
		public synchronized void setMessage(String message) {
			this.message = message;
		}
		
		public synchronized String getMessage() {
			return message;
		}
		
		public synchronized void addChangesetId(String changesetid) {
			this.changesetIds.add(changesetid);
			this.executingChangesetId = changesetid;
		}
		
		public synchronized List<String> getChangesetIds() {
			return changesetIds;
		}
		
		public synchronized String getExecutingChangesetId() {
			return executingChangesetId;
		}
		
		/**
		 * @return the database updater Warnings
		 */
		public synchronized List<String> getUpdateWarnings() {
			return updateWarnings;
		}
		
		public synchronized boolean hasWarnings() {
			return hasUpdateWarnings;
		}
		
		public synchronized void reportWarnings(List<String> warnings) {
			updateWarnings.addAll(warnings);
			hasUpdateWarnings = true;
		}
		
		/**
		 * This class does all the work of creating the desired database, user, updates, etc
		 */
		public UpdateFilterCompletion() {
			Runnable r = new Runnable() {
				
				/**
				 * TODO split this up into multiple testable methods
				 *
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					try {
						/**
						 * A callback class that prints out info about liquibase changesets
						 */
						class PrintingChangeSetExecutorCallback implements ChangeSetExecutorCallback {
							
							private String message;
							
							public PrintingChangeSetExecutorCallback(String message) {
								this.message = message;
							}
							
							/**
							 * @see ChangeSetExecutorCallback#executing(liquibase.changelog.ChangeSet, int)
							 */
							@Override
							public void executing(ChangeSet changeSet, int numChangeSetsToRun) {
								addChangesetId(changeSet.getId());
								setMessage(message);
							}
							
						}
						
						try {
							setMessage("Updating the database to the latest version");
							
							ChangeLogDetective changeLogDetective = new ChangeLogDetective();
							ChangeLogVersionFinder changeLogVersionFinder = new ChangeLogVersionFinder();
							
							List<String> changelogs = new ArrayList<>();
							List<String> warnings = new ArrayList<>();
							
							String version = changeLogDetective.getInitialLiquibaseSnapshotVersion(DatabaseUpdater.CONTEXT,
							    new DatabaseUpdaterLiquibaseProvider());
							
							log.debug(
							    "updating the database with versions of liquibase-update-to-latest files greater than '{}'",
							    version);
							
							changelogs.addAll(changeLogVersionFinder
							        .getUpdateFileNames(changeLogVersionFinder.getUpdateVersionsGreaterThan(version)));
							
							log.debug("found applicable Liquibase update change logs: {}", changelogs);
							
							for (String changelog : changelogs) {
								log.debug("applying Liquibase changelog '{}'", changelog);
								
								List<String> currentWarnings = DatabaseUpdater.executeChangelog(changelog,
								    new PrintingChangeSetExecutorCallback("executing Liquibase changelog :" + changelog));
								
								if (currentWarnings != null) {
									warnings.addAll(currentWarnings);
								}
							}
							executingChangesetId = null; // clear out the last changeset
							
							if (CollectionUtils.isNotEmpty(warnings)) {
								reportWarnings(warnings);
							}
						}
						catch (InputRequiredException inputRequired) {
							// the user would be stepped through the questions returned here.
							log.error("Not implemented", inputRequired);
							updateFilterModel.updateChanges();
							reportError(ErrorMessageConstants.UPDATE_ERROR_INPUT_NOT_IMPLEMENTED,
							    inputRequired.getMessage());
							return;
						}
						catch (DatabaseUpdateException e) {
							log.error("Unable to update the database", e);
							Map<String, Object[]> databaseUpdateErrors = new HashMap<>();
							databaseUpdateErrors.put(ErrorMessageConstants.UPDATE_ERROR_UNABLE, null);
							for (String errorMessage : Arrays.asList(e.getMessage().split("\n"))) {
								databaseUpdateErrors.put(errorMessage, null);
							}
							updateFilterModel.updateChanges();
							reportErrors(databaseUpdateErrors);
							return;
						}
						catch (Exception e) {
							log.error("Unable to update the database", e);
							return;
						}
						
						setMessage("Starting OpenMRS");
						try {
							startOpenmrs(filterConfig.getServletContext());
						}
						catch (Exception e) {
							log.error("Unable to complete the startup.", e);
							reportError(ErrorMessageConstants.UPDATE_ERROR_COMPLETE_STARTUP, e.getMessage());
							return;
						}
						
						// set this so that the wizard isn't run again on next page load
						setUpdatesRequired(false);
					}
					finally {
						if (!hasErrors()) {
							setUpdatesRequired(false);
						}
						//reset to let other user's make requests after updates are run
						isDatabaseUpdateInProgress = false;
					}
				}
			};
			
			thread = new Thread(r);
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/util/LocalizationTool.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.generic.ResourceTool;
import org.openmrs.util.LocaleUtility;

/**
 * This class is intended for accessing {@link ResourceBundle} and formatting messages therein.
 */
@DefaultKey("l10n")
public class LocalizationTool extends ResourceTool {
	
	/**
	 * The default message resource bundle to use, this is english
	 */
	private static ResourceBundle defaultResourceBundle = null;
	
	/**
	 * Its need to override base class method to be able to change its locale property outside the
	 * class hierarchy
	 *
	 * @see org.apache.velocity.tools.generic.ResourceTool#setLocale(Locale locale)
	 */
	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
	}
	
	/**
	 * @return the defaultResourceBundle
	 */
	public static ResourceBundle getDefaultResourceBundle() {
		if (defaultResourceBundle == null) {
			defaultResourceBundle = CustomResourceLoader.getInstance(null).getResourceBundle(Locale.ENGLISH);
		}
		return defaultResourceBundle;
	}
	
	/**
	 * To be able to load resource bundles outside the class path we need to override this method
	 *
	 * @see org.apache.velocity.tools.generic.ResourceTool#getBundle(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	protected ResourceBundle getBundle(String baseName, Object loc) {
		Locale locale = (loc == null) ? getLocale() : LocaleUtility.fromSpecification(String.valueOf(loc));
		if (baseName == null || locale == null) {
			return null;
		}
		//This messages_XX.properties file doesn't exist, default to messages.properties
		ResourceBundle rb = CustomResourceLoader.getInstance(null).getResourceBundle(locale);
		if (rb == null) {
			rb = getDefaultResourceBundle();
		}
		
		return rb;
	}
	
	/**
	 * @see org.apache.velocity.tools.generic.ResourceTool#get(java.lang.Object, java.lang.String[],
	 *      java.lang.Object)
	 */
	@Override
	public Object get(Object code, String[] resourceNamePrefixes, Object locale) {
		Object msg = super.get(code, resourceNamePrefixes, locale);
		//if code's translation is blank, use the english equivalent
		if (msg == null || StringUtils.isBlank(msg.toString())) {
			msg = super.get(code, resourceNamePrefixes, Locale.ENGLISH.toString());
		}
		
		return msg;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/util/CustomResourceLoader.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.util.LocaleUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * This class is responsible for loading messages resources from file system
 */
public class CustomResourceLoader {
	
	private static final Logger log = LoggerFactory.getLogger(CustomResourceLoader.class);
	
	/** */
	public static final String PREFIX = "messages";
	
	/** the map that contains resource bundles for each locale */
	private Map<Locale, ResourceBundle> resources;
	
	/** the set of languages, which is currently supported */
	private Set<Locale> availablelocales;
	
	private static CustomResourceLoader instance = null;
	
	/**
	 * default constructor that initializes inner map of resources
	 */
	private CustomResourceLoader(HttpServletRequest httpRequest) {
		this.resources = new HashMap<>();
		this.availablelocales = new HashSet<>();
		
		try {
			PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
			Resource[] localResources = patternResolver.getResources("classpath*:messages*.properties");
			for (Resource localeResource : localResources) {
				Locale locale = parseLocaleFrom(localeResource.getFilename(), PREFIX);
				ResourceBundle rb = new PropertyResourceBundle(new InputStreamReader(localeResource.getInputStream(), StandardCharsets.UTF_8));
				getResource().put(locale, rb);
				getAvailablelocales().add(locale);
			}
		}
		catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Returns singleton instance of custom resource loader
	 *
	 * @param httpRequest <b>(optional)</b> the absolute path to directory, that contains resources to
	 *            be loaded. If this isn't specified then <code>${CONTEXT-ROOT}/WEB-INF/</code> will
	 *            be used
	 * @return the singleton instance of {@link CustomResourceLoader}
	 */
	public static CustomResourceLoader getInstance(HttpServletRequest httpRequest) {
		if (instance == null) {
			instance = new CustomResourceLoader(httpRequest);
		}
		return instance;
	}
	
	/**
	 * Utility method for deriving a locale from a filename.
	 *
	 * @param filename the name to parse
	 * @return Locale derived from the given string
	 */
	private Locale parseLocaleFrom(String filename, String basename) {
		Locale result;
		String tempFilename = filename;
		
		if (filename.startsWith(basename)) {
			tempFilename = filename.substring(basename.length());
		}
		
		String localespec = tempFilename.substring(0, tempFilename.indexOf('.'));
		
		if ("".equals(localespec)) {
			result = Locale.ENGLISH;
		} else {
			localespec = localespec.substring(1);
			result = LocaleUtility.fromSpecification(localespec);
		}
		return result;
	}
	
	/**
	 * @param locale the locale for which will be retrieved resource bundle
	 * @return resource bundle for specified locale
	 */
	public ResourceBundle getResourceBundle(Locale locale) {
		return resources.get(locale);
	}
	
	/**
	 * @return the map object, which contains locale as key and resources bundle for each locale as
	 *         value
	 */
	public Map<Locale, ResourceBundle> getResource() {
		return resources;
	}
	
	/**
	 * @return the set of locales which are currently supported by OpenMRS
	 */
	public Set<Locale> getAvailablelocales() {
		return availablelocales;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/util/ErrorMessageConstants.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.util;

/**
 * This class contains all constants that describes names of properties, which are used as keys,
 * when showing localized error messages on pages of install and database wizard
 */
public class ErrorMessageConstants {
	
	private ErrorMessageConstants() {
	}
	
	public static final String ERROR_DB_PSDW_REQ = "install.error.dbPasswd";
	
	public static final String ERROR_DB_DRIVER_CLASS_REQ = "install.error.dbDriverClass";
	
	public static final String ERROR_DB_CONN_REQ = "install.error.dbConn";
	
	public static final String ERROR_DB_DRIVER_REQ = "install.error.dbDriver";
	
	public static final String ERROR_DB_CURR_NAME_REQ = "install.error.dbCurrName";
	
	public static final String ERROR_DB_NEW_NAME_REQ = "install.error.dbNewName";
	
	public static final String ERROR_DB_USER_NAME_REQ = "install.error.dbUserName";
	
	public static final String ERROR_DB_USER_PSWD_REQ = "install.error.dbUserPswd";
	
	public static final String ERROR_DB_CUR_USER_NAME_REQ = "install.error.dbCurUserName";
	
	public static final String ERROR_DB_CUR_USER_PSWD_REQ = "install.error.dbCurUserPswd";
	
	public static final String ERROR_DB_ADM_PSWDS_MATCH = "install.error.adminPswdMatch";
	
	public static final String ERROR_DB_ADM_PSDW_EMPTY = "install.error.adminPswdEmpty";
	
	public static final String ERROR_DB_ADM_PSDW_WEAK = "install.error.adminPswdWeak";
	
	public static final String ERROR_DB_IMPL_ID_REQ = "install.error.implID";
	
	public static final String ERROR_DB_CONN_VERIFY = "install.error.connVerify";
	
	public static final String ERROR_DB_EXEC_SQL = "install.error.execSql";
	
	public static final String ERROR_DB_CREATE_NEW = "install.error.unableCreateDb";
	
	public static final String ERROR_DB_CREATE_DB_USER = "install.error.unableCreateDbUser";
	
	public static final String ERROR_DB_GRANT_PRIV = "install.error.unableGranPrivileges";
	
	public static final String ERROR_DB_CONNECTION_FAIL = "install.error.unableConnect";
	
	public static final String ERROR_DB_IMPORT_TEST_DATA = "install.error.unableImportTestData";
	
	public static final String ERROR_DB_CREATE_TABLES_OR_ADD_DEMO_DATA = "install.error.unableCreateTablesOrAddDemoData";
	
	public static final String ERROR_DB_UPDATE_TO_LATEST = "install.error.unableUpdateToLatest";
	
	public static final String ERROR_DB_UPDATE = "install.error.unableUpdate";
	
	public static final String ERROR_INPUT_REQ = "install.error.inputRequired";
	
	public static final String ERROR_MANDATORY_MOD_REQ = "install.error.mandatoryModuleRequired";
	
	public static final String ERROR_CORE_MOD_REQ = "install.error.coreModuleRequired";
	
	public static final String ERROR_SET_INPL_ID = "install.error.unableSetImplId";
	
	public static final String ERROR_COMPLETE_STARTUP = "install.error.unableCompleteStartup";
	
	public static final String ERROR_UNABLE_COPY_DATA = "install.error.unableCopyData";
	
	public static final String ERROR_UNABLE_CREATE_DB = "install.error.unableCreateDb";
	
	public static final String ERROR_UNABLE_CREATE_DUMP = "install.error.unableCreateDump";
	
	public static final String ERROR_UNABLE_CREATE_ENV = "install.error.unableCreateEnvironment";
	
	public static final String UPDATE_ERROR_COMPLETE_STARTUP = "update.error.unableCompleteStartup";
	
	public static final String UPDATE_ERROR_INPUT_NOT_IMPLEMENTED = "update.error.inpuNotImplemented";
	
	public static final String UPDATE_ERROR_UNABLE = "update.error.unableUpdate";
	
	public static final String UPDATE_ERROR_UNABLE_AUTHENTICATE = "update.error.unableAuthenticate";
	
	public static final String ERROR_DB_UNABLE_TO_ADD_MODULES = "install.error.failedToAddModules";
	
	public static final String ERROR_DB_UNABLE_TO_FETCH_MODULES = "install.error.failedToFetchModules";
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/util/FilterUtil.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.util.DatabaseUpdater;
import org.openmrs.util.DatabaseUtil;
import org.openmrs.util.OpenmrsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains convenient methods for storing/retrieving locale parameters into/from DB as
 * admin's user property and as default locale property for OpenMRS system
 */
public class FilterUtil {
	
	private FilterUtil() {
	}
	
	private static final Logger log = LoggerFactory.getLogger(FilterUtil.class);
	
	private static final String DATABASE_CLOSING_ERROR = "Error while closing the database";
	
	public static final String LOCALE_ATTRIBUTE = "locale";
	
	public static final String REMEMBER_ATTRIBUTE = "remember";
	
	public static final String ADMIN_USERNAME = "admin";
	
	/**
	 * Tries to retrieve location parameter. First this method makes an attempt to load locale
	 * parameter as user's property. And next, if user's property is empty it tries to retrieve
	 * default system locale (i.e system global property). If it also is empty it uses default value
	 * for system locale
	 *
	 * @param username the name of the administrative user whose default locale property will be
	 *            restored
	 * @return string with stored location parameter or default OpenMRS locale property's value
	 */
	public static String restoreLocale(String username) {
		String currentLocale = null;
		if (StringUtils.isNotBlank(username)) {
			PreparedStatement statement = null;
			Connection connection = null;
			ResultSet results = null;
			try {
				connection = DatabaseUpdater.getConnection();
				
				// first we should try to get locale parameter as user's property
				Integer userId = getUserIdByName(username, connection);
				
				if (userId != null) {
					String select = "select property_value from user_property where user_id = ? and property = ?";
					statement = connection.prepareStatement(select);
					statement.setInt(1, userId);
					statement.setString(2, OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE);
					if (statement.execute()) {
						results = statement.getResultSet();
						if (results.next()) {
							currentLocale = results.getString(1);
						}
					}
				}
				
				// if locale is still null we should try to retrieve system locale global property's value
				if (currentLocale == null) {
					currentLocale = readSystemDefaultLocale(connection);
				}
			}
			catch (Exception e) {
				log.error("Error while retriving locale property", e);
			}
			finally {
				try {
					if (statement != null) {
						statement.close();
					}
				}
				catch (SQLException e) {
					log.warn("Error while closing statement");
				}
				
				if (connection != null) {
					try {
						connection.close();
					}
					catch (SQLException e) {
						log.debug(DATABASE_CLOSING_ERROR, e);
					}
				}
				
				if (results != null) {
					try {
						results.close();
					}
					catch (SQLException e) {
						log.warn("Error while closing ResultSet", e);
					}
				}
			}
		}
		// if locale is still null we just simply using default locale value (i.e. en_GB)
		if (currentLocale == null) {
			currentLocale = OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE_DEFAULT_VALUE;
		}
		
		return currentLocale;
	}
	
	/**
	 * This method uses passed in connection to load system default locale. If connection is passed
	 * as null it creates separate connection that should be closed before return from method
	 *
	 * @param connection (optional) the jdbc connection to be used for extracting default locale
	 * @return the string that contains system default locale or null
	 */
	public static String readSystemDefaultLocale(Connection connection) {
		String systemDefaultLocale = null;
		boolean needToCloseConection = false;
		try {
			if (connection == null) {
				connection = DatabaseUpdater.getConnection();
				needToCloseConection = true;
			}
			String select = "select property_value from global_property where property = ?";
			PreparedStatement statement = connection.prepareStatement(select);
			statement.setString(1, OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE);
			if (statement.execute()) {
				ResultSet results = statement.getResultSet();
				if (results.next()) {
					systemDefaultLocale = results.getString(1);
				}
			}
		}
		catch (Exception e) {
			log.error("Error while retrieving system default locale", e);
		}
		finally {
			if (needToCloseConection && connection != null) {
				try {
					connection.close();
				}
				catch (SQLException e) {
					log.debug(DATABASE_CLOSING_ERROR, e);
				}
			}
		}
		return systemDefaultLocale;
	}
	
	/**
	 * Stores selected by user locale into DB as admin's user property and as system default locale
	 *
	 * @param locale the selected by user language
	 * @return true if locale was stored successfully
	 */
	public static boolean storeLocale(String locale) {
		if (StringUtils.isNotBlank(locale)) {
			
			Connection connection = null;
			Integer userId = null;
			try {
				connection = DatabaseUpdater.getConnection();
				
				// first we should try to get admin user id
				userId = getUserIdByName(ADMIN_USERNAME, connection);
				
				// first we are saving locale as administrative user's property
				if (userId != null) {
					String insert = "insert into user_property (user_id, property, property_value) values (?, 'defaultLocale', ?)";
					PreparedStatement statement = null;
					try {
						statement = connection.prepareStatement(insert);
						statement.setInt(1, userId);
						statement.setString(2, locale);
						if (statement.executeUpdate() != 1) {
							log.warn("Unable to save user locale as admin property.");
						}
					}
					finally {
						if (statement != null) {
							try {
								statement.close();
							}
							catch (Exception statementCloseEx) {
								log.error("Failed to quietly close Statement", statementCloseEx);
							}
						}
					}
					
				}
				
				// and the second step is to save locale as system default locale global property
				String update = "update global_property set property_value = ? where property = ? ";
				PreparedStatement statement = null;
				try {
					statement = connection.prepareStatement(update);
					statement.setString(1, locale);
					statement.setString(2, OpenmrsConstants.GLOBAL_PROPERTY_DEFAULT_LOCALE);
					if (statement.executeUpdate() != 1) {
						log.warn("Unable to set system default locale property.");
					}
				}
				finally {
					if (statement != null) {
						try {
							statement.close();
						}
						catch (Exception statementCloseEx) {
							log.error("Failed to quietly close Statement", statementCloseEx);
						}
					}
				}
			}
			catch (Exception e) {
				log.warn("Locale " + locale + " could not be set for user with id " + userId + " .", e);
				return false;
			}
			finally {
				if (connection != null) {
					try {
						connection.close();
					}
					catch (SQLException e) {
						log.debug(DATABASE_CLOSING_ERROR, e);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * This is a utility method that can be used for retrieving user id by given user name and sql
	 * connection
	 *
	 * @param userNameOrSystemId the name of user
	 * @param connection the java sql connection to use
	 * @return not null id of given user in case of success or null otherwise
	 * @throws SQLException
	 */
	public static Integer getUserIdByName(String userNameOrSystemId, Connection connection) throws SQLException {
		
		String select = "select user_id from users where system_id = ? or username = ?";
		PreparedStatement statement = connection.prepareStatement(select);
		statement.setString(1, userNameOrSystemId);
		statement.setString(2, userNameOrSystemId);
		Integer userId = null;
		if (statement.execute()) {
			ResultSet results = statement.getResultSet();
			if (results.next()) {
				userId = results.getInt(1);
			}
		}
		return userId;
	}
	
	/**
	 * Gets the value of a global Property as a string from the database using sql, this method is
	 * useful when you want to get a value of a global property before the application context has
	 * been setup
	 *
	 * @param globalPropertyName the name of the global property
	 * @return the global property value
	 */
	public static String getGlobalPropertyValue(String globalPropertyName) {
		String propertyValue = null;
		Connection connection = null;
		
		try {
			connection = DatabaseUpdater.getConnection();
			List<List<Object>> results = DatabaseUtil.executeSQL(connection,
			    "select property_value from global_property where property = '" + globalPropertyName + "'", true);
			if (results.size() == 1 && results.get(0).size() == 1) {
				propertyValue = results.get(0).get(0).toString();
			}
		}
		catch (Exception e) {
			log.error("Error while retrieving value for global property:" + globalPropertyName, e);
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				}
				catch (SQLException e) {
					log.debug("Error while closing the database connection", e);
				}
			}
		}
		
		return propertyValue;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/startuperror/StartupErrorFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.startuperror;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleUtil;
import org.openmrs.module.OpenmrsCoreModuleException;
import org.openmrs.web.Listener;
import org.openmrs.web.filter.StartupFilter;

/**
 * This is the second filter that is processed. It is only active when OpenMRS has some liquibase
 * updates that need to be run. If updates are needed, this filter/wizard asks for a super user to
 * authenticate and review the updates before continuing.
 */
public class StartupErrorFilter extends StartupFilter {
	
	/**
	 * The velocity macro page to redirect to if an error occurs or on initial startup
	 */
	private static final String DEFAULT_PAGE = "generalerror.vm";
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on GET requests
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	        throws IOException, ServletException {
		
		if (getUpdateFilterModel().errorAtStartup instanceof OpenmrsCoreModuleException) {
			renderTemplate("coremoduleerror.vm", new HashMap<>(), httpResponse);
		} else {
			renderTemplate(DEFAULT_PAGE, new HashMap<>(), httpResponse);
		}
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	        throws IOException, ServletException {
		// if they are uploading modules
		if (getUpdateFilterModel().errorAtStartup instanceof OpenmrsCoreModuleException) {
			RequestContext requestContext = new ServletRequestContext(httpRequest);
			if (!ServletFileUpload.isMultipartContent(requestContext)) {
				throw new ServletException("The request is not a valid multipart/form-data upload request");
			}
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				Context.openSession();
				List<FileItem> items = upload.parseRequest(requestContext);
				for (FileItem item : items) {
					InputStream uploadedStream = item.getInputStream();
					ModuleUtil.insertModuleFile(uploadedStream, item.getName());
				}
			}
			catch (FileUploadException ex) {
				throw new ServletException("Error while uploading file(s)", ex);
			}
			finally {
				Context.closeSession();
			}
			
			Map<String, Object> map = new HashMap<>();
			map.put("success", Boolean.TRUE);
			renderTemplate("coremoduleerror.vm", map, httpResponse);
			
			// TODO restart openmrs here instead of going to coremodulerror template
		}
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#getUpdateFilterModel()
	 */
	@Override
	protected StartupErrorFilterModel getUpdateFilterModel() {
		// this object was initialized in the #init(FilterConfig) method
		return new StartupErrorFilterModel(Listener.getErrorAtStartup());
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#skipFilter(HttpServletRequest)
	 */
	@Override
	public boolean skipFilter(HttpServletRequest request) {
		return !Listener.errorOccurredAtStartup();
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#getTemplatePrefix()
	 */
	@Override
	protected String getTemplatePrefix() {
		return "org/openmrs/web/filter/startuperror/";
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/startuperror/StartupErrorFilterModel.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.startuperror;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openmrs.web.filter.StartupFilter;
import org.openmrs.web.filter.update.UpdateFilter;

/**
 * The {@link UpdateFilter} uses this model object to hold all properties that are edited by the
 * user in the wizard. All attributes on this model object are added to all templates rendered by
 * the {@link StartupFilter}.
 */
public class StartupErrorFilterModel {
	
	public Throwable errorAtStartup;
	
	public String stacktrace;
	
	/**
	 * Default constructor that sets up some of the properties
	 */
	public StartupErrorFilterModel(Throwable t) {
		errorAtStartup = t;
		stacktrace = ExceptionUtils.getStackTrace(t);
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/initialization/TestInstallUtil.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.initialization;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleConstants;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.filter.util.FilterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains static methods to be used by the installation wizard when creating a testing
 * installation
 */
public class TestInstallUtil {
	private TestInstallUtil() {
	}
	
	private static final Logger log = LoggerFactory.getLogger(TestInstallUtil.class);
	
	/**
	 * Adds data to the test database from a sql dump file
	 *
	 * @param host
	 * @param port
	 * @param databaseName
	 * @param user
	 * @param pwd
	 * @return true if data was added successfully
	 */
	protected static boolean addTestData(String host, int port, String databaseName, String user, String pwd, String filePath) {
		Process proc;
		BufferedReader br = null;
		String errorMsg = null;
		String[] command = new String[] { "mysql", "--host=" + host, "--port=" + port, "--user=" + user,
		        "--password=" + pwd, "--database=" + databaseName, "-e", "source " + filePath };
		
		//For stand-alone, use explicit path to the mysql executable.
		String runDirectory = System.getProperties().getProperty("user.dir");
		File file = Paths.get(runDirectory, "database", "bin", "mysql").toFile();
		
		if (file.exists()) {
			command[0] = file.getAbsolutePath();
		}
		
		try {
			proc = Runtime.getRuntime().exec(command);
			try {
				br = new BufferedReader(new InputStreamReader(proc.getErrorStream(), StandardCharsets.UTF_8));
				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null) {
					sb.append(System.getProperty("line.separator"));
					sb.append(line);
				}
				errorMsg = sb.toString();
			}
			catch (IOException e) {
				log.error("Failed to add test data:", e);
			}
			finally {
				if (br != null) {
					try {
						br.close();
					}
					catch (Exception e) {
						log.error("Failed to close the inputstream:", e);
					}
				}
			}
			
			//print out the error messages from the process
			if (StringUtils.isNotBlank(errorMsg)) {
				log.error(errorMsg);
			}
			
			if (proc.waitFor() == 0) {
				log.debug("Added test data successfully");
				return true;
			}
			
			log
	        .error("The process terminated abnormally while adding test data. Please look under the Configuration section at: https://wiki.openmrs.org/display/docs/Release+Testing+Helper+Module");
			
		}
		catch (IOException e) {
			log.error("Failed to create the sql dump", e);
		}
		catch (InterruptedException e) {
			log.error("The back up was interrupted while adding test data", e);
		}
		
		return false;
	}
	
	/**
	 * Extracts .omod files from the specified {@link InputStream} and copies them to the module
	 * repository of the test application data directory, the method always closes the InputStream
	 * before returning
	 *
	 * @param in the {@link InputStream} for the zip file
	 */
	@SuppressWarnings("rawtypes")
	protected static boolean addZippedTestModules(InputStream in) {
		ZipFile zipFile = null;
		FileOutputStream out = null;
		File tempFile = null;
		boolean successfullyAdded = true;
		
		try {
			tempFile = File.createTempFile("modules", null);
			out = new FileOutputStream(tempFile);
			IOUtils.copy(in, out);
			zipFile = new ZipFile(tempFile);
			Enumeration entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					log.debug("Skipping directory: {}", entry.getName());
					continue;
				}
				
				String fileName = entry.getName();
				if (fileName.endsWith(".omod")) {
					//Convert the names of .omod files located in nested directories so that they get
					//created under the module repo directory when being copied
					if (fileName.contains(System.getProperty("file.separator"))) {
						fileName = new File(entry.getName()).getName();
					}
					
					log.debug("Extracting module file: {}", fileName);
					
					//use the module repository folder GP value if specified
					String moduleRepositoryFolder = FilterUtil
					        .getGlobalPropertyValue(ModuleConstants.REPOSITORY_FOLDER_PROPERTY);
					if (StringUtils.isBlank(moduleRepositoryFolder)) {
						moduleRepositoryFolder = ModuleConstants.REPOSITORY_FOLDER_PROPERTY_DEFAULT;
					}
					
					//At this point 'OpenmrsConstants.APPLICATION_DATA_DIRECTORY' is still null so we need check
					//for the app data directory defined in the runtime props file if any otherwise the logic in
					//the OpenmrsUtil.getDirectoryInApplicationDataDirectory(String) will default to the other
					String appDataDirectory = Context.getRuntimeProperties().getProperty(
					    OpenmrsConstants.APPLICATION_DATA_DIRECTORY_RUNTIME_PROPERTY);
					if (StringUtils.isNotBlank(appDataDirectory)) {
						OpenmrsUtil.setApplicationDataDirectory(appDataDirectory);
					}
					
					File moduleRepository = OpenmrsUtil.getDirectoryInApplicationDataDirectory(moduleRepositoryFolder);
					
					//delete all previously added modules in case of prior test installations
					FileUtils.cleanDirectory(moduleRepository);

					final File zipEntryFile = new File(moduleRepository, fileName);

					if (!zipEntryFile.toPath().normalize().startsWith(moduleRepository.toPath().normalize())) {
						throw new IOException("Bad zip entry");
					}
					
					OpenmrsUtil.copyFile(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(zipEntryFile)));
				} else {
					log.debug("Ignoring file that is not a .omod '{}'", fileName);
				}
			}
		}
		catch (IOException e) {
			log.error("An error occured while copying modules to the test system:", e);
			successfullyAdded = false;
		}
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
			if (zipFile != null) {
				try {
					zipFile.close();
				}
				catch (IOException e) {
					log.error("Failed to close zip file: ", e);
				}
			}
			if (tempFile != null) {
				tempFile.delete();
			}
		}
		
		return successfullyAdded;
	}
	
	/**
	 * Tests the connection to the specified URL
	 *
	 * @param urlString the url to test
	 * @return true if a connection a established otherwise false
	 */
	protected static boolean testConnection(String urlString) {
		try {
			HttpURLConnection urlConnect = (HttpURLConnection) new URL(urlString).openConnection();
			//wait for 15sec
			urlConnect.setConnectTimeout(15000);
			urlConnect.setUseCaches(false);
			//trying to retrieve data from the source. If there
			//is no connection, this line will fail
			urlConnect.getContent();
			return true;
		}
		catch (IOException e) {
			log.debug("Error generated:", e);
		}
		
		return false;
	}
	
	/**
	 * @param url
	 * @param openmrsUsername
	 * @param openmrsPassword
	 * @return input stream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected static InputStream getResourceInputStream(String url, String openmrsUsername, String openmrsPassword)
	        throws MalformedURLException, IOException, APIException {
		
		HttpURLConnection connection = createConnection(url);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
		out.write(encodeCredentials(openmrsUsername, openmrsPassword));
		out.flush();
		out.close();
		
		log.info("Http response message: {}, Code: {}", connection.getResponseMessage(), connection.getResponseCode());
		
		if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			throw new APIAuthenticationException("Invalid username or password");
		} else if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
			throw new APIException("error.occurred.on.remote.server", (Object[]) null);
		}
		
		return connection.getInputStream();
	}
	private static HttpURLConnection createConnection(String url) 
			throws IOException, MalformedURLException {
		final HttpURLConnection result = (HttpURLConnection) new URL(url).openConnection();
		result.setRequestMethod("POST");
		result.setConnectTimeout(15000);
		result.setUseCaches(false);
		result.setDoOutput(true);
		return result;
	}
	private static String encodeCredentials(String openmrsUsername, String openmrsPassword) {
		final StringBuilder result = new StringBuilder();
		result.append("username=");
		final Encoder encoder = Base64.getEncoder();
		final Charset utf8 = StandardCharsets.UTF_8;
		result.append(new String(encoder.encode(openmrsUsername.getBytes(utf8)), utf8));
		result.append("&password=");
		result.append(new String(encoder.encode(openmrsPassword.getBytes(utf8)), utf8));
		return result.toString();
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/initialization/DatabaseDetective.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.initialization;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

import org.openmrs.util.DatabaseUtil;

public class DatabaseDetective {
	
	private static final String CONNECTION_URL = "connection.url";
	
	private static final String CONNECTION_USERNAME = "connection.username";
	
	private static final String CONNECTION_PASSWORD = "connection.password";
	
	/**
	 * Check whether openmrs database is empty. Having just one non-liquibase table in the given
	 * database qualifies this as a non-empty database.
	 *
	 * @param props the runtime properties
	 * @return true if the openmrs database is empty or does not exist yet
	 */
	public boolean isDatabaseEmpty(Properties props) {
		if (props == null) {
			return true;
		}
		
		Connection connection = null;
		
		try {
			DatabaseUtil.loadDatabaseDriver(props.getProperty(CONNECTION_URL), null);
			
			connection = DriverManager.getConnection(props.getProperty(CONNECTION_URL), props
			        .getProperty(CONNECTION_USERNAME), props.getProperty(CONNECTION_PASSWORD));
			
			DatabaseMetaData dbMetaData = connection.getMetaData();
			
			String[] types = { "TABLE" };
			
			//get all tables
			ResultSet tbls = dbMetaData.getTables(null, null, null, types);
			
			while (tbls.next()) {
				String tableName = tbls.getString("TABLE_NAME");
				//if any table exist besides "liquibasechangelog" or "liquibasechangeloglock", return false
				if (!("liquibasechangelog".equals(tableName.toLowerCase()))
				        && !("liquibasechangeloglock".equals(tableName.toLowerCase()))) {
					return false;
				}
			}
			return true;
		}
		catch (Exception e) {
			// consider the database to be empty
			return true;
		}
		finally {
			try {
				if (connection != null) {
					connection.close();
				}
			}
			catch (Exception e) {
				// consider the database to be empty
				return true;
			}
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/initialization/WizardTask.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.initialization;

/**
 * The different tasks that the wizard could be executing at a given time during the initialization
 * process.
 */
public enum WizardTask {
	
	CREATE_SCHEMA("install.progress.tasks.create.schema"),
	CREATE_DB_USER("install.progress.tasks.create.user"),
	
	CREATE_TABLES("install.progress.tasks.create.tables"),
	ADD_CORE_DATA("install.progress.tasks.add.coreData"),
	
	ADD_DEMO_DATA("install.progress.tasks.add.demoData"),
	UPDATE_TO_LATEST("install.progress.tasks.update"),
	
	IMPORT_TEST_DATA("install.progress.tasks.test"),
	ADD_MODULES("install.progress.tasks.addModules");
	
	private final String displayText;
	
	/**
	 * Constructor
	 * 
	 * @param displayText The displayText for the enum value
	 */
	private WizardTask(String displayText) {
		this.displayText = displayText;
	}
	
	/**
	 * Returns the displayText to be printed in the IU
	 * 
	 * @return The displayText for the enum value
	 */
	public String displayText() {
		return this.displayText;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/initialization/InitializationWizardModel.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.initialization;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.util.OpenmrsConstants;
import org.openmrs.web.WebConstants;

/**
 * The {@link InitializationFilter} uses this model object to hold all properties that are edited by
 * the user in the wizard. All attributes on this model object are added to all templates rendered
 * by the {@link InitializationFilter}.
 */
public class InitializationWizardModel {
	
	// automatically given to the .vm files and used there
	public static final String HEADER_TEMPLATE = "org/openmrs/web/filter/initialization/header.vm";
	
	// automatically given to the .vm files and used there
	public static final String FOOTER_TEMPLATE = "org/openmrs/web/filter/initialization/footer.vm";
	
	// Values for installMethod field.
	public static final String INSTALL_METHOD_SIMPLE = "simple";
	
	public static final String INSTALL_METHOD_ADVANCED = "advanced";
	
	public static final String INSTALL_METHOD_TESTING = "testing";
	
	public static final String INSTALL_METHOD_AUTO = "auto";
	
	// Default OpenMRS admin password set by the simple installation.
	public static final String ADMIN_DEFAULT_PASSWORD = "Admin123";
	
	public static final String OPENMRS_VERSION = OpenmrsConstants.OPENMRS_VERSION_SHORT;
	
	/**
	 * Default database name to use unless user specifies another in the wizard or they are creating
	 * a test installation
	 */
	public static final String DEFAULT_DATABASE_NAME = WebConstants.WEBAPP_NAME;
	
	/**
	 * Records completed tasks and are displayed at the top of the page upon error
	 */
	public List<String> workLog = new ArrayList<>();
	
	/**
	 * Whether the runtime properties file could possible be created. (only read by the velocity
	 * scripts)
	 */
	
	public boolean canCreate = true;
	
	/**
	 * Error message from not being able to create the runtime properties file (only read by the
	 * velocity scripts)
	 */
	
	public String cannotCreateErrorMessage = "";
	
	/**
	 * Whether the runtime file can be edited (only read by the velocity scripts)
	 */
	
	public boolean canWrite = true;
	
	/**
	 * The location of the runtime properties file (only read by the velocity scripts)
	 */
	
	public String runtimePropertiesPath = "";
	
	public String installMethod = INSTALL_METHOD_SIMPLE;
	
	/**
	 * True/false marker for the question "Do you currently have an OpenMRS database installed"
	 */
	public Boolean hasCurrentOpenmrsDatabase = true;
	
	/**
	 * True/false marker for the
	 * question"Do you currently have a database user other than root that has read/write access"
	 */
	public Boolean hasCurrentDatabaseUser = true;
	
	/**
	 * Filled out by the user on the databasesetup.vm page
	 */
	public String databaseName = DEFAULT_DATABASE_NAME;
	
	/**
	 * Filled out by user on the databasesetup.vm page Looks like:
	 */
	public String databaseConnection = "jdbc:mysql://localhost:3306/@DBNAME@?autoReconnect=true&sessionVariables=default_storage_engine=InnoDB&useUnicode=true&characterEncoding=UTF-8";
	
	/**
	 * Optional Database Driver string filled in on databasesetup.vm
	 */
	public String databaseDriver = "";
	
	/**
	 * MySQL root account password used for simple installation. Filled in simplesetup.vm.
	 */
	public String databaseRootPassword = "";
	
	/**
	 * Filled in on databasesetup.vm
	 */
	public String createDatabaseUsername = "root";
	
	/**
	 * Filled in on databasesetup.vm
	 */
	public String createDatabasePassword = "";
	
	/**
	 * DB user that can create an openmrs db user Filled in on databasetablesanduser.vm
	 */
	public String createUserUsername = "root";
	
	/**
	 * DB user that can create an openmrs db user Filled in on databasetablesanduser.vm
	 */
	public String createUserPassword = "";
	
	/**
	 * The username of a user that exists that can read/write to openmrs. Entered on
	 * databasetablesanduser page
	 */
	public String currentDatabaseUsername = "";
	
	/**
	 * The password of a user that exists that can read/write to openmrs. Entered on
	 * databasetablesanduser page
	 */
	public String currentDatabasePassword = "";
	
	/**
	 * Asked for on the databasetablesanduser.vm page to know if their existing database has the
	 * tables or not
	 */
	public Boolean createTables = Boolean.FALSE;
	
	/**
	 * if the user asked us to create the user for openmrs
	 */
	public Boolean createDatabaseUser = Boolean.FALSE;
	
	/**
	 * Enables importing test data from the remote server
	 */
	public Boolean importTestData = Boolean.FALSE;
	
	/**
	 * Does the user want to add the demo data to the database?
	 */
	public Boolean addDemoData = Boolean.FALSE;
	
	/**
	 * Asked for on the otherproperties.vm page to know if the allow_web_admin runtime property is
	 * true/false
	 */
	public Boolean moduleWebAdmin = Boolean.TRUE;
	
	/**
	 * Asked for on otherproperties.vm page to know if the runtime property for auto updating their
	 * db is true/false
	 */
	public Boolean autoUpdateDatabase = Boolean.FALSE;
	
	/**
	 * Password for the admin user if the database was created now
	 */
	public String adminUserPassword = ADMIN_DEFAULT_PASSWORD;
	
	/**
	 * Implementation name.
	 */
	public String implementationIdName = "";
	
	/**
	 * Implementation ID.
	 */
	public String implementationId = "";
	
	/**
	 * Pass phrase used to validate who uses your implementation ID.
	 */
	public String implementationIdPassPhrase = "";
	
	/**
	 * Text describing the implementation.
	 */
	public String implementationIdDescription = "";
	
	public String setupPageUrl = WebConstants.SETUP_PAGE_URL;
	
	/**
	 * The tasks to be executed that the user selected from the wizard's prompts
	 */
	public List<WizardTask> tasksToExecute;
	
	public String localeToSave = "";
	
	/**
	 * The url to the remote system
	 */
	public String remoteUrl = "";
	
	/**
	 * The username to use to authenticate to the remote system
	 */
	public String remoteUsername = "";
	
	/**
	 * The password to use to authenticate to the remote system
	 */
	public String remotePassword = "";
	
	/**
	 * The current step. e.g Step 1 of ...
	 */
	public Integer currentStepNumber = 1;
	
	/**
	 * The total number of steps. e.g Step ... of 5
	 */
	public Integer numberOfSteps = 1;
}

File path: openmrs-core/web/src/main/java/org/openmrs/web/filter/initialization/InitializationFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.web.filter.initialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.zip.ZipInputStream;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import liquibase.changelog.ChangeSet;
import org.apache.commons.io.IOUtils;
import org.openmrs.ImplementationId;
import org.openmrs.api.APIAuthenticationException;
import org.openmrs.api.PasswordException;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.api.context.UsernamePasswordCredentials;
import org.openmrs.api.impl.UserServiceImpl;
import org.openmrs.liquibase.ChangeLogDetective;
import org.openmrs.liquibase.ChangeLogVersionFinder;
import org.openmrs.module.MandatoryModuleException;
import org.openmrs.module.OpenmrsCoreModuleException;
import org.openmrs.module.web.WebModuleUtil;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.DatabaseUpdater;
import org.openmrs.liquibase.ChangeSetExecutorCallback;
import org.openmrs.util.DatabaseUpdaterLiquibaseProvider;
import org.openmrs.util.DatabaseUtil;
import org.openmrs.util.InputRequiredException;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.util.Security;
import org.openmrs.web.Listener;
import org.openmrs.web.WebConstants;
import org.openmrs.web.WebDaemon;
import org.openmrs.web.filter.StartupFilter;
import org.openmrs.web.filter.update.UpdateFilter;
import org.openmrs.web.filter.util.CustomResourceLoader;
import org.openmrs.web.filter.util.ErrorMessageConstants;
import org.openmrs.web.filter.util.FilterUtil;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

/**
 * This is the first filter that is processed. It is only active when starting OpenMRS for the very
 * first time. It will redirect all requests to the {@link WebConstants#SETUP_PAGE_URL} if the
 * {@link Listener} wasn't able to find any runtime properties
 */
public class InitializationFilter extends StartupFilter {
	
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(InitializationFilter.class);
	
	private static final String DATABASE_POSTGRESQL = "postgresql";
	
	private static final String DATABASE_MYSQL = "mysql";
	
	private static final String DATABASE_SQLSERVER = "sqlserver";
	
	private static final String DATABASE_H2 = "h2";
	
	private static final String LIQUIBASE_DEMO_DATA = "liquibase-demo-data.xml";
	
	/**
	 * The very first page of wizard, that asks user for select his preferred language
	 */
	private static final String CHOOSE_LANG = "chooselang.vm";
	
	/**
	 * The second page of the wizard that asks for simple or advanced installation.
	 */
	private static final String INSTALL_METHOD = "installmethod.vm";
	
	/**
	 * The simple installation setup page.
	 */
	private static final String SIMPLE_SETUP = "simplesetup.vm";
	
	/**
	 * The first page of the advanced installation of the wizard that asks for a current or past
	 * database
	 */
	private static final String DATABASE_SETUP = "databasesetup.vm";
	
	/**
	 * The page from where the user specifies the url to a remote system, username and password
	 */
	private static final String TESTING_REMOTE_DETAILS_SETUP = "remotedetails.vm";
	
	/**
	 * The velocity macro page to redirect to if an error occurs or on initial startup
	 */
	private static final String DEFAULT_PAGE = CHOOSE_LANG;
	
	/**
	 * This page asks whether database tables/demo data should be inserted and what the
	 * username/password that will be put into the runtime properties is
	 */
	private static final String DATABASE_TABLES_AND_USER = "databasetablesanduser.vm";
	
	/**
	 * This page lets the user define the admin user
	 */
	private static final String ADMIN_USER_SETUP = "adminusersetup.vm";
	
	/**
	 * This page lets the user pick an implementation id
	 */
	private static final String IMPLEMENTATION_ID_SETUP = "implementationidsetup.vm";
	
	/**
	 * This page asks for settings that will be put into the runtime properties files
	 */
	private static final String OTHER_RUNTIME_PROPS = "otherruntimeproperties.vm";
	
	/**
	 * A page that tells the user that everything is collected and will now be processed
	 */
	private static final String WIZARD_COMPLETE = "wizardcomplete.vm";
	
	/**
	 * A page that lists off what is happening while it is going on. This page has ajax that callst he
	 * {@value #PROGRESS_VM_AJAXREQUEST} page
	 */
	private static final String PROGRESS_VM = "progress.vm";
	
	/**
	 * This url is called by javascript to get the status of the install
	 */
	private static final String PROGRESS_VM_AJAXREQUEST = "progress.vm.ajaxRequest";
	
	public static final String RELEASE_TESTING_MODULE_PATH = "/module/releasetestinghelper/";
	
	/**
	 * The model object that holds all the properties that the rendered templates use. All attributes on
	 * this object are made available to all templates via reflection in the
	 * {@link org.openmrs.web.filter.StartupFilter#renderTemplate(String, Map, HttpServletResponse)} method.
	 */
	private InitializationWizardModel wizardModel = null;
	
	private InitializationCompletion initJob;
	
	/**
	 * Variable set to true as soon as the installation begins and set to false when the process ends
	 * This thread should only be accesses through the synchronized method.
	 */
	private static boolean isInstallationStarted = false;
	
	// the actual driver loaded by the DatabaseUpdater class
	private String loadedDriverString;
	
	/**
	 * Variable set at the end of the wizard when spring is being restarted
	 */
	private static boolean initializationComplete = false;
	
	protected synchronized void setInitializationComplete(boolean initializationComplete) {
		InitializationFilter.initializationComplete = initializationComplete;
	}
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on GET requests
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	@Override
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
		throws IOException, ServletException {
		loadInstallationScriptIfPresent();
		
		// we need to save current user language in references map since it will be used when template
		// will be rendered
		if (httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE) == null) {
			checkLocaleAttributesForFirstTime(httpRequest);
		}
		
		Map<String, Object> referenceMap = new HashMap<>();
		String page = httpRequest.getParameter("page");
		
		referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE, httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
		
		httpResponse.setHeader("Cache-Control", "no-cache");
		
		// if any body has already started installation and this is not an ajax request for the progress
		if (isInstallationStarted() && !PROGRESS_VM_AJAXREQUEST.equals(page)) {
			referenceMap.put("isInstallationStarted", true);
			httpResponse.setContentType("text/html");
			renderTemplate(PROGRESS_VM, referenceMap, httpResponse);
		} else if (PROGRESS_VM_AJAXREQUEST.equals(page)) {
			httpResponse.setContentType("text/json");
			Map<String, Object> result = new HashMap<>();
			if (initJob != null) {
				result.put("hasErrors", initJob.hasErrors());
				if (initJob.hasErrors()) {
					result.put("errorPage", initJob.getErrorPage());
					errors.putAll(initJob.getErrors());
				}
				
				result.put("initializationComplete", isInitializationComplete());
				result.put("message", initJob.getMessage());
				result.put("actionCounter", initJob.getStepsComplete());
				if (!isInitializationComplete()) {
					result.put("executingTask", initJob.getExecutingTask());
					result.put("executedTasks", initJob.getExecutedTasks());
					result.put("completedPercentage", initJob.getCompletedPercentage());
				}
				
				addLogLinesToResponse(result);
			}
			
			PrintWriter writer = httpResponse.getWriter();
			writer.write(toJSONString(result));
			writer.close();
		} else if (InitializationWizardModel.INSTALL_METHOD_AUTO.equals(wizardModel.installMethod)
			|| httpRequest.getServletPath().equals("/" + AUTO_RUN_OPENMRS)) {
			autoRunOpenMRS(httpRequest);
			referenceMap.put("isInstallationStarted", true);
			httpResponse.setContentType("text/html");
			renderTemplate(PROGRESS_VM, referenceMap, httpResponse);
		} else if (page == null) {
			httpResponse.setContentType("text/html");// if any body has already started installation
			
			//If someone came straight here without setting the hidden page input,
			// then we need to clear out all the passwords
			clearPasswords();
			
			renderTemplate(DEFAULT_PAGE, referenceMap, httpResponse);
		} else if (INSTALL_METHOD.equals(page)) {
			// get props and render the second page
			File runtimeProperties = getRuntimePropertiesFile();
			
			if (!runtimeProperties.exists()) {
				try {
					runtimeProperties.createNewFile();
					// reset the error objects in case of refresh
					wizardModel.canCreate = true;
					wizardModel.cannotCreateErrorMessage = "";
				}
				catch (IOException io) {
					wizardModel.canCreate = false;
					wizardModel.cannotCreateErrorMessage = io.getMessage();
				}
				
				// check this before deleting the file again
				wizardModel.canWrite = runtimeProperties.canWrite();
				
				// delete the file again after testing the create/write
				// so that if the user stops the webapp before finishing
				// this wizard, they can still get back into it
				runtimeProperties.delete();
				
			} else {
				wizardModel.canWrite = runtimeProperties.canWrite();
				
				wizardModel.databaseConnection = Context.getRuntimeProperties().getProperty("connection.url",
					wizardModel.databaseConnection);
				
				wizardModel.currentDatabaseUsername = Context.getRuntimeProperties().getProperty("connection.username",
					wizardModel.currentDatabaseUsername);
				
				wizardModel.currentDatabasePassword = Context.getRuntimeProperties().getProperty("connection.password",
					wizardModel.currentDatabasePassword);
			}
			
			wizardModel.runtimePropertiesPath = runtimeProperties.getAbsolutePath();
			
			// do step one of the wizard
			httpResponse.setContentType("text/html");
			renderTemplate(INSTALL_METHOD, referenceMap, httpResponse);
		}
	}
	
	private void loadInstallationScriptIfPresent() {
		Properties script = getInstallationScript();
		if (!script.isEmpty()) {
			wizardModel.installMethod = script.getProperty("install_method", wizardModel.installMethod);
			
			wizardModel.databaseConnection = script.getProperty("connection.url", wizardModel.databaseConnection);
			wizardModel.databaseDriver = script.getProperty("connection.driver_class", wizardModel.databaseDriver);
			wizardModel.currentDatabaseUsername = script.getProperty("connection.username",
				wizardModel.currentDatabaseUsername);
			wizardModel.currentDatabasePassword = script.getProperty("connection.password",
				wizardModel.currentDatabasePassword);
			
			String hasCurrentOpenmrsDatabase = script.getProperty("has_current_openmrs_database");
			if (hasCurrentOpenmrsDatabase != null) {
				wizardModel.hasCurrentOpenmrsDatabase = Boolean.valueOf(hasCurrentOpenmrsDatabase);
			}
			wizardModel.createDatabaseUsername = script.getProperty("create_database_username",
				wizardModel.createDatabaseUsername);
			wizardModel.createDatabasePassword = script.getProperty("create_database_password",
				wizardModel.createDatabasePassword);
			
			String createTables = script.getProperty("create_tables");
			if (createTables != null) {
				wizardModel.createTables = Boolean.valueOf(createTables);
			}
			
			String createDatabaseUser = script.getProperty("create_database_user");
			if (createDatabaseUser != null) {
				wizardModel.createDatabaseUser = Boolean.valueOf(createDatabaseUser);
			}
			wizardModel.createUserUsername = script.getProperty("create_user_username", wizardModel.createUserUsername);
			wizardModel.createUserPassword = script.getProperty("create_user_password", wizardModel.createUserPassword);
			
			String addDemoData = script.getProperty("add_demo_data");
			if (addDemoData != null) {
				wizardModel.addDemoData = Boolean.valueOf(addDemoData);
			}
			
			String moduleWebAdmin = script.getProperty("module_web_admin");
			if (moduleWebAdmin != null) {
				wizardModel.moduleWebAdmin = Boolean.valueOf(moduleWebAdmin);
			}
			
			String autoUpdateDatabase = script.getProperty("auto_update_database");
			if (autoUpdateDatabase != null) {
				wizardModel.autoUpdateDatabase = Boolean.valueOf(autoUpdateDatabase);
			}
			
			wizardModel.adminUserPassword = script.getProperty("admin_user_password", wizardModel.adminUserPassword);
		}
	}
	
	private void clearPasswords() {
		wizardModel.databaseRootPassword = "";
		wizardModel.createDatabasePassword = "";
		wizardModel.createUserPassword = "";
		wizardModel.currentDatabasePassword = "";
		wizardModel.remotePassword = "";
	}
	
	/**
	 * Called by {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} on POST requests
	 *
	 * @param httpRequest
	 * @param httpResponse
	 */
	@Override
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
		throws IOException, ServletException {
		String page = httpRequest.getParameter("page");
		Map<String, Object> referenceMap = new HashMap<>();
		// we need to save current user language in references map since it will be used when template
		// will be rendered
		if (httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE) != null) {
			referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE,
				httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
		}
		
		// if any body has already started installation
		if (isInstallationStarted()) {
			referenceMap.put("isInstallationStarted", true);
			httpResponse.setContentType("text/html");
			renderTemplate(PROGRESS_VM, referenceMap, httpResponse);
			return;
		}
		if (DEFAULT_PAGE.equals(page)) {
			// get props and render the first page
			File runtimeProperties = getRuntimePropertiesFile();
			if (!runtimeProperties.exists()) {
				try {
					runtimeProperties.createNewFile();
					// reset the error objects in case of refresh
					wizardModel.canCreate = true;
					wizardModel.cannotCreateErrorMessage = "";
				}
				catch (IOException io) {
					wizardModel.canCreate = false;
					wizardModel.cannotCreateErrorMessage = io.getMessage();
				}
				// check this before deleting the file again
				wizardModel.canWrite = runtimeProperties.canWrite();
				
				// delete the file again after testing the create/write
				// so that if the user stops the webapp before finishing
				// this wizard, they can still get back into it
				runtimeProperties.delete();
			} else {
				wizardModel.canWrite = runtimeProperties.canWrite();
				
				wizardModel.databaseConnection = Context.getRuntimeProperties().getProperty("connection.url",
					wizardModel.databaseConnection);
				
				wizardModel.currentDatabaseUsername = Context.getRuntimeProperties().getProperty("connection.username",
					wizardModel.currentDatabaseUsername);
				
				wizardModel.currentDatabasePassword = Context.getRuntimeProperties().getProperty("connection.password",
					wizardModel.currentDatabasePassword);
			}
			
			wizardModel.runtimePropertiesPath = runtimeProperties.getAbsolutePath();
			
			checkLocaleAttributes(httpRequest);
			referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE,
				httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
			log.info("Locale stored in session is " + httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
			
			httpResponse.setContentType("text/html");
			// otherwise do step one of the wizard
			renderTemplate(INSTALL_METHOD, referenceMap, httpResponse);
		} else if (INSTALL_METHOD.equals(page)) {
			if (goBack(httpRequest)) {
				referenceMap.put(FilterUtil.REMEMBER_ATTRIBUTE,
					httpRequest.getSession().getAttribute(FilterUtil.REMEMBER_ATTRIBUTE) != null);
				referenceMap.put(FilterUtil.LOCALE_ATTRIBUTE,
					httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE));
				renderTemplate(CHOOSE_LANG, referenceMap, httpResponse);
				return;
			}
			wizardModel.installMethod = httpRequest.getParameter("install_method");
			if (InitializationWizardModel.INSTALL_METHOD_SIMPLE.equals(wizardModel.installMethod)) {
				page = SIMPLE_SETUP;
			} else if (InitializationWizardModel.INSTALL_METHOD_TESTING.equals(wizardModel.installMethod)) {
				page = TESTING_REMOTE_DETAILS_SETUP;
				wizardModel.currentStepNumber = 1;
				wizardModel.numberOfSteps = skipDatabaseSetupPage() ? 1 : 3;
			} else {
				page = DATABASE_SETUP;
				wizardModel.currentStepNumber = 1;
				wizardModel.numberOfSteps = 5;
			}
			renderTemplate(page, referenceMap, httpResponse);
		} // simple method
		else if (SIMPLE_SETUP.equals(page)) {
			if (goBack(httpRequest)) {
				renderTemplate(INSTALL_METHOD, referenceMap, httpResponse);
				return;
			}
			wizardModel.databaseConnection = httpRequest.getParameter("database_connection");
			;
			
			wizardModel.createDatabaseUsername = Context.getRuntimeProperties().getProperty("connection.username",
				wizardModel.createDatabaseUsername);
			
			wizardModel.createUserUsername = wizardModel.createDatabaseUsername;
			
			wizardModel.databaseRootPassword = httpRequest.getParameter("database_root_password");
			checkForEmptyValue(wizardModel.databaseRootPassword, errors, ErrorMessageConstants.ERROR_DB_PSDW_REQ);
			
			wizardModel.hasCurrentOpenmrsDatabase = false;
			wizardModel.createTables = true;
			// default wizardModel.databaseName is openmrs
			// default wizardModel.createDatabaseUsername is root
			wizardModel.createDatabasePassword = wizardModel.databaseRootPassword;
			wizardModel.addDemoData = "yes".equals(httpRequest.getParameter("add_demo_data"));
			
			wizardModel.hasCurrentDatabaseUser = false;
			wizardModel.createDatabaseUser = true;
			// default wizardModel.createUserUsername is root
			wizardModel.createUserPassword = wizardModel.databaseRootPassword;
			
			wizardModel.moduleWebAdmin = true;
			wizardModel.autoUpdateDatabase = false;
			
			wizardModel.adminUserPassword = InitializationWizardModel.ADMIN_DEFAULT_PASSWORD;
			
			createSimpleSetup(httpRequest.getParameter("database_root_password"), httpRequest.getParameter("add_demo_data"));
			
			try {
				loadedDriverString = DatabaseUtil.loadDatabaseDriver(wizardModel.databaseConnection,
					wizardModel.databaseDriver);
			}
			catch (ClassNotFoundException e) {
				errors.put(ErrorMessageConstants.ERROR_DB_DRIVER_CLASS_REQ, null);
				renderTemplate(page, referenceMap, httpResponse);
				return;
			}
			
			if (errors.isEmpty()) {
				page = WIZARD_COMPLETE;
			}
			renderTemplate(page, referenceMap, httpResponse);
		} // step one
		else if (DATABASE_SETUP.equals(page)) {
			if (goBack(httpRequest)) {
				wizardModel.currentStepNumber -= 1;
				if (InitializationWizardModel.INSTALL_METHOD_TESTING.equals(wizardModel.installMethod)) {
					renderTemplate(TESTING_REMOTE_DETAILS_SETUP, referenceMap, httpResponse);
				} else {
					renderTemplate(INSTALL_METHOD, referenceMap, httpResponse);
				}
				return;
			}
			
			wizardModel.databaseConnection = httpRequest.getParameter("database_connection");
			checkForEmptyValue(wizardModel.databaseConnection, errors, ErrorMessageConstants.ERROR_DB_CONN_REQ);
			
			wizardModel.databaseDriver = httpRequest.getParameter("database_driver");
			checkForEmptyValue(wizardModel.databaseConnection, errors, ErrorMessageConstants.ERROR_DB_DRIVER_REQ);
			
			loadedDriverString = loadDriver(wizardModel.databaseConnection, wizardModel.databaseDriver);
			if (!StringUtils.hasText(loadedDriverString)) {
				errors.put(ErrorMessageConstants.ERROR_DB_DRIVER_CLASS_REQ, null);
				renderTemplate(page, referenceMap, httpResponse);
				return;
			}
			
			//TODO make each bit of page logic a (unit testable) method
			
			// asked the user for their desired database name
			
			if ("yes".equals(httpRequest.getParameter("current_openmrs_database"))) {
				wizardModel.databaseName = httpRequest.getParameter("openmrs_current_database_name");
				checkForEmptyValue(wizardModel.databaseName, errors, ErrorMessageConstants.ERROR_DB_CURR_NAME_REQ);
				wizardModel.hasCurrentOpenmrsDatabase = true;
				// TODO check to see if this is an active database
				
			} else {
				// mark this wizard as a "to create database" (done at the end)
				wizardModel.hasCurrentOpenmrsDatabase = false;
				
				wizardModel.createTables = true;
				
				wizardModel.databaseName = httpRequest.getParameter("openmrs_new_database_name");
				checkForEmptyValue(wizardModel.databaseName, errors, ErrorMessageConstants.ERROR_DB_NEW_NAME_REQ);
				// TODO create database now to check if its possible?
				
				wizardModel.createDatabaseUsername = httpRequest.getParameter("create_database_username");
				checkForEmptyValue(wizardModel.createDatabaseUsername, errors, ErrorMessageConstants.ERROR_DB_USER_NAME_REQ);
				wizardModel.createDatabasePassword = httpRequest.getParameter("create_database_password");
				checkForEmptyValue(wizardModel.createDatabasePassword, errors, ErrorMessageConstants.ERROR_DB_USER_PSWD_REQ);
			}
			
			if (errors.isEmpty()) {
				page = DATABASE_TABLES_AND_USER;
				
				if (InitializationWizardModel.INSTALL_METHOD_TESTING.equals(wizardModel.installMethod)) {
					wizardModel.currentStepNumber = 3;
				} else {
					wizardModel.currentStepNumber = 2;
				}
			}
			
			renderTemplate(page, referenceMap, httpResponse);
			
		} // step two
		else if (DATABASE_TABLES_AND_USER.equals(page)) {
			
			if (goBack(httpRequest)) {
				wizardModel.currentStepNumber -= 1;
				renderTemplate(DATABASE_SETUP, referenceMap, httpResponse);
				return;
			}
			
			if (wizardModel.hasCurrentOpenmrsDatabase) {
				wizardModel.createTables = "yes".equals(httpRequest.getParameter("create_tables"));
			}
			
			wizardModel.addDemoData = "yes".equals(httpRequest.getParameter("add_demo_data"));
			
			if ("yes".equals(httpRequest.getParameter("current_database_user"))) {
				wizardModel.currentDatabaseUsername = httpRequest.getParameter("current_database_username");
				checkForEmptyValue(wizardModel.currentDatabaseUsername, errors,
					ErrorMessageConstants.ERROR_DB_CUR_USER_NAME_REQ);
				wizardModel.currentDatabasePassword = httpRequest.getParameter("current_database_password");
				checkForEmptyValue(wizardModel.currentDatabasePassword, errors,
					ErrorMessageConstants.ERROR_DB_CUR_USER_PSWD_REQ);
				wizardModel.hasCurrentDatabaseUser = true;
				wizardModel.createDatabaseUser = false;
			} else {
				wizardModel.hasCurrentDatabaseUser = false;
				wizardModel.createDatabaseUser = true;
				// asked for the root mysql username/password
				wizardModel.createUserUsername = httpRequest.getParameter("create_user_username");
				checkForEmptyValue(wizardModel.createUserUsername, errors, ErrorMessageConstants.ERROR_DB_USER_NAME_REQ);
				wizardModel.createUserPassword = httpRequest.getParameter("create_user_password");
				checkForEmptyValue(wizardModel.createUserPassword, errors, ErrorMessageConstants.ERROR_DB_USER_PSWD_REQ);
			}
			
			if (errors.isEmpty()) { // go to next page
				page = InitializationWizardModel.INSTALL_METHOD_TESTING.equals(wizardModel.installMethod) ? WIZARD_COMPLETE
					: OTHER_RUNTIME_PROPS;
			}
			
			renderTemplate(page, referenceMap, httpResponse);
		} // step three
		else if (OTHER_RUNTIME_PROPS.equals(page)) {
			
			if (goBack(httpRequest)) {
				renderTemplate(DATABASE_TABLES_AND_USER, referenceMap, httpResponse);
				return;
			}
			
			wizardModel.moduleWebAdmin = "yes".equals(httpRequest.getParameter("module_web_admin"));
			wizardModel.autoUpdateDatabase = "yes".equals(httpRequest.getParameter("auto_update_database"));
			
			if (wizardModel.createTables) { // go to next page if they are creating tables
				page = ADMIN_USER_SETUP;
			} else { // skip a page
				page = IMPLEMENTATION_ID_SETUP;
			}
			
			renderTemplate(page, referenceMap, httpResponse);
			
		} // optional step four
		else if (ADMIN_USER_SETUP.equals(page)) {
			
			if (goBack(httpRequest)) {
				renderTemplate(OTHER_RUNTIME_PROPS, referenceMap, httpResponse);
				return;
			}
			
			wizardModel.adminUserPassword = httpRequest.getParameter("new_admin_password");
			String adminUserConfirm = httpRequest.getParameter("new_admin_password_confirm");
			
			// throw back to admin user if passwords don't match
			if (!wizardModel.adminUserPassword.equals(adminUserConfirm)) {
				errors.put(ErrorMessageConstants.ERROR_DB_ADM_PSWDS_MATCH, null);
				renderTemplate(ADMIN_USER_SETUP, referenceMap, httpResponse);
				return;
			}
			
			// throw back if the user didn't put in a password
			if ("".equals(wizardModel.adminUserPassword)) {
				errors.put(ErrorMessageConstants.ERROR_DB_ADM_PSDW_EMPTY, null);
				renderTemplate(ADMIN_USER_SETUP, referenceMap, httpResponse);
				return;
			}
			
			try {
				OpenmrsUtil.validatePassword("admin", wizardModel.adminUserPassword, "admin");
			}
			catch (PasswordException p) {
				errors.put(ErrorMessageConstants.ERROR_DB_ADM_PSDW_WEAK, null);
				renderTemplate(ADMIN_USER_SETUP, referenceMap, httpResponse);
				return;
			}
			
			if (errors.isEmpty()) { // go to next page
				page = IMPLEMENTATION_ID_SETUP;
			}
			
			renderTemplate(page, referenceMap, httpResponse);
			
		} // optional step five
		else if (IMPLEMENTATION_ID_SETUP.equals(page)) {
			
			if (goBack(httpRequest)) {
				if (wizardModel.createTables) {
					renderTemplate(ADMIN_USER_SETUP, referenceMap, httpResponse);
				} else {
					renderTemplate(OTHER_RUNTIME_PROPS, referenceMap, httpResponse);
				}
				return;
			}
			
			wizardModel.implementationIdName = httpRequest.getParameter("implementation_name");
			wizardModel.implementationId = httpRequest.getParameter("implementation_id");
			wizardModel.implementationIdPassPhrase = httpRequest.getParameter("pass_phrase");
			wizardModel.implementationIdDescription = httpRequest.getParameter("description");
			
			// throw back if the user-specified ID is invalid (contains ^ or |).
			if (wizardModel.implementationId.indexOf('^') != -1 || wizardModel.implementationId.indexOf('|') != -1) {
				errors.put(ErrorMessageConstants.ERROR_DB_IMPL_ID_REQ, null);
				renderTemplate(IMPLEMENTATION_ID_SETUP, referenceMap, httpResponse);
				return;
			}
			
			if (errors.isEmpty()) { // go to next page
				page = WIZARD_COMPLETE;
			}
			
			renderTemplate(page, referenceMap, httpResponse);
		} else if (WIZARD_COMPLETE.equals(page)) {
			
			if (goBack(httpRequest)) {
				
				if (InitializationWizardModel.INSTALL_METHOD_SIMPLE.equals(wizardModel.installMethod)) {
					page = SIMPLE_SETUP;
				} else if (InitializationWizardModel.INSTALL_METHOD_TESTING.equals(wizardModel.installMethod)) {
					if (skipDatabaseSetupPage()) {
						page = TESTING_REMOTE_DETAILS_SETUP;
					} else {
						page = DATABASE_TABLES_AND_USER;
					}
				} else {
					page = IMPLEMENTATION_ID_SETUP;
				}
				renderTemplate(page, referenceMap, httpResponse);
				return;
			}
			
			wizardModel.tasksToExecute = new ArrayList<>();
			createDatabaseTask();
			if (InitializationWizardModel.INSTALL_METHOD_TESTING.equals(wizardModel.installMethod)) {
				wizardModel.importTestData = true;
				wizardModel.createTables = false;
				wizardModel.addDemoData = false;
				//if we have a runtime properties file
				if (skipDatabaseSetupPage()) {
					wizardModel.hasCurrentOpenmrsDatabase = false;
					wizardModel.hasCurrentDatabaseUser = true;
					wizardModel.createDatabaseUser = false;
					Properties props = OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME);
					wizardModel.currentDatabaseUsername = props.getProperty("connection.username");
					wizardModel.currentDatabasePassword = props.getProperty("connection.password");
					wizardModel.createDatabaseUsername = wizardModel.currentDatabaseUsername;
					wizardModel.createDatabasePassword = wizardModel.currentDatabasePassword;
				}
				
				wizardModel.tasksToExecute.add(WizardTask.IMPORT_TEST_DATA);
				wizardModel.tasksToExecute.add(WizardTask.ADD_MODULES);
			} else {
				createTablesTask();
				createDemoDataTask();
			}
			wizardModel.tasksToExecute.add(WizardTask.UPDATE_TO_LATEST);
			
			referenceMap.put("tasksToExecute", wizardModel.tasksToExecute);
			startInstallation();
			renderTemplate(PROGRESS_VM, referenceMap, httpResponse);
		} else if (TESTING_REMOTE_DETAILS_SETUP.equals(page)) {
			if (goBack(httpRequest)) {
				wizardModel.currentStepNumber -= 1;
				renderTemplate(INSTALL_METHOD, referenceMap, httpResponse);
				return;
			}
			
			wizardModel.remoteUrl = httpRequest.getParameter("remoteUrl");
			checkForEmptyValue(wizardModel.remoteUrl, errors, "install.testing.remote.url.required");
			if (errors.isEmpty()) {
				//Check if the remote system is running
				if (TestInstallUtil.testConnection(wizardModel.remoteUrl)) {
					//Check if the test module is installed by connecting to its setting page
					if (TestInstallUtil
						.testConnection(wizardModel.remoteUrl.concat(RELEASE_TESTING_MODULE_PATH + "settings.htm"))) {
						
						wizardModel.remoteUsername = httpRequest.getParameter("username");
						wizardModel.remotePassword = httpRequest.getParameter("password");
						checkForEmptyValue(wizardModel.remoteUsername, errors, "install.testing.username.required");
						checkForEmptyValue(wizardModel.remotePassword, errors, "install.testing.password.required");
						
						if (errors.isEmpty()) {
							//check if the username and password are valid
							try {
								TestInstallUtil.getResourceInputStream(
									wizardModel.remoteUrl + RELEASE_TESTING_MODULE_PATH + "verifycredentials.htm",
									wizardModel.remoteUsername, wizardModel.remotePassword);
							}
							catch (APIAuthenticationException e) {
								log.debug("Error generated: ", e);
								page = TESTING_REMOTE_DETAILS_SETUP;
								errors.put(ErrorMessageConstants.UPDATE_ERROR_UNABLE_AUTHENTICATE, null);
								renderTemplate(page, referenceMap, httpResponse);
								return;
							}
							
							//If we have a runtime properties file, get the database setup details from it
							if (skipDatabaseSetupPage()) {
								Properties props = OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME);
								wizardModel.databaseConnection = props.getProperty("connection.url");
								loadedDriverString = loadDriver(wizardModel.databaseConnection, wizardModel.databaseDriver);
								if (!StringUtils.hasText(loadedDriverString)) {
									page = TESTING_REMOTE_DETAILS_SETUP;
									errors.put(ErrorMessageConstants.ERROR_DB_DRIVER_CLASS_REQ, null);
									renderTemplate(page, referenceMap, httpResponse);
									return;
								}
								
								wizardModel.databaseName = InitializationWizardModel.DEFAULT_DATABASE_NAME;
								page = WIZARD_COMPLETE;
							} else {
								page = DATABASE_SETUP;
								wizardModel.currentStepNumber = 2;
							}
							msgs.put("install.testing.testingModuleFound", null);
						} else {
							renderTemplate(page, referenceMap, httpResponse);
							return;
						}
					} else {
						errors.put("install.testing.noTestingModule", null);
					}
				} else {
					errors.put("install.testing.invalidProductionUrl", new Object[] { wizardModel.remoteUrl });
				}
			}
			
			renderTemplate(page, referenceMap, httpResponse);
		}
	}
	
	private void startInstallation() {
		//if no one has run any installation
		if (!isInstallationStarted()) {
			initJob = new InitializationCompletion();
			setInstallationStarted(true);
			initJob.start();
		}
	}
	
	private void createDemoDataTask() {
		if (wizardModel.addDemoData) {
			wizardModel.tasksToExecute.add(WizardTask.ADD_DEMO_DATA);
		}
	}
	
	private void createTablesTask() {
		if (wizardModel.createTables) {
			wizardModel.tasksToExecute.add(WizardTask.CREATE_TABLES);
			wizardModel.tasksToExecute.add(WizardTask.ADD_CORE_DATA);
		}
	}
	
	private void createDatabaseTask() {
		if (!wizardModel.hasCurrentOpenmrsDatabase) {
			wizardModel.tasksToExecute.add(WizardTask.CREATE_SCHEMA);
		}
		if (wizardModel.createDatabaseUser) {
			wizardModel.tasksToExecute.add(WizardTask.CREATE_DB_USER);
		}
	}
	
	private void createSimpleSetup(String databaseRootPassword, String addDemoData) {
		setDatabaseNameIfInTestMode();
		wizardModel.databaseConnection = Context.getRuntimeProperties().getProperty("connection.url",
			wizardModel.databaseConnection);
		
		wizardModel.createDatabaseUsername = Context.getRuntimeProperties().getProperty("connection.username",
			wizardModel.createDatabaseUsername);
		
		wizardModel.createUserUsername = wizardModel.createDatabaseUsername;
		
		wizardModel.databaseRootPassword = databaseRootPassword;
		checkForEmptyValue(wizardModel.databaseRootPassword, errors, ErrorMessageConstants.ERROR_DB_PSDW_REQ);
		
		wizardModel.hasCurrentOpenmrsDatabase = false;
		wizardModel.createTables = true;
		// default wizardModel.databaseName is openmrs
		// default wizardModel.createDatabaseUsername is root
		wizardModel.createDatabasePassword = wizardModel.databaseRootPassword;
		wizardModel.addDemoData = "yes".equals(addDemoData);
		
		wizardModel.hasCurrentDatabaseUser = false;
		wizardModel.createDatabaseUser = true;
		// default wizardModel.createUserUsername is root
		wizardModel.createUserPassword = wizardModel.databaseRootPassword;
		
		wizardModel.moduleWebAdmin = true;
		wizardModel.autoUpdateDatabase = false;
		
		wizardModel.adminUserPassword = InitializationWizardModel.ADMIN_DEFAULT_PASSWORD;
	}
	
	private void setDatabaseNameIfInTestMode() {
		if (OpenmrsUtil.isTestMode()) {
			wizardModel.databaseName = OpenmrsUtil.getOpenMRSVersionInTestMode();
		}
	}
	
	private void autoRunOpenMRS(HttpServletRequest httpRequest) {
		File runtimeProperties = getRuntimePropertiesFile();
		wizardModel.runtimePropertiesPath = runtimeProperties.getAbsolutePath();
		
		if (!InitializationWizardModel.INSTALL_METHOD_AUTO.equals(wizardModel.installMethod)) {
			if (httpRequest.getParameter("database_user_name") != null) {
				wizardModel.createDatabaseUsername = httpRequest.getParameter("database_user_name");
			}
			
			createSimpleSetup(httpRequest.getParameter("database_root_password"), "yes");
		}
		
		checkLocaleAttributes(httpRequest);
		try {
			loadedDriverString = DatabaseUtil.loadDatabaseDriver(wizardModel.databaseConnection, wizardModel.databaseDriver);
		}
		catch (ClassNotFoundException e) {
			errors.put(ErrorMessageConstants.ERROR_DB_DRIVER_CLASS_REQ, null);
			return;
		}
		wizardModel.tasksToExecute = new ArrayList<>();
		createDatabaseTask();
		createTablesTask();
		createDemoDataTask();
		wizardModel.tasksToExecute.add(WizardTask.UPDATE_TO_LATEST);
		startInstallation();
	}
	
	/**
	 * This method should be called after the user has left wizard's first page (i.e. choose language).
	 * It checks if user has changed any of locale related parameters and makes appropriate corrections
	 * with filter's model or/and with locale attribute inside user's session.
	 *
	 * @param httpRequest the http request object
	 */
	private void checkLocaleAttributes(HttpServletRequest httpRequest) {
		String localeParameter = httpRequest.getParameter(FilterUtil.LOCALE_ATTRIBUTE);
		Boolean rememberLocale = false;
		// we need to check if user wants that system will remember his selection of language
		if (httpRequest.getParameter(FilterUtil.REMEMBER_ATTRIBUTE) != null) {
			rememberLocale = true;
		}
		if (localeParameter != null) {
			String storedLocale = null;
			if (httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE) != null) {
				storedLocale = httpRequest.getSession().getAttribute(FilterUtil.LOCALE_ATTRIBUTE).toString();
			}
			// if user has changed locale parameter to new one
			// or chooses it parameter at first page loading
			if (storedLocale == null || !storedLocale.equals(localeParameter)) {
				log.info("Stored locale parameter to session " + localeParameter);
				httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, localeParameter);
			}
			if (rememberLocale) {
				httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, localeParameter);
				httpRequest.getSession().setAttribute(FilterUtil.REMEMBER_ATTRIBUTE, true);
				wizardModel.localeToSave = localeParameter;
			} else {
				// we need to reset it if it was set before
				httpRequest.getSession().setAttribute(FilterUtil.REMEMBER_ATTRIBUTE, null);
				wizardModel.localeToSave = null;
			}
		}
	}
	
	/**
	 * It sets locale parameter for current session when user is making first GET http request to
	 * application. It retrieves user locale from request object and checks if this locale is supported
	 * by application. If not, it uses {@link Locale#ENGLISH} by default
	 *
	 * @param httpRequest the http request object
	 */
	public void checkLocaleAttributesForFirstTime(HttpServletRequest httpRequest) {
		Locale locale = httpRequest.getLocale();
		if (CustomResourceLoader.getInstance(httpRequest).getAvailablelocales().contains(locale)) {
			httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, locale.toString());
		} else {
			httpRequest.getSession().setAttribute(FilterUtil.LOCALE_ATTRIBUTE, Locale.ENGLISH.toString());
		}
	}
	
	/**
	 * Verify the database connection works.
	 *
	 * @param connectionUsername
	 * @param connectionPassword
	 * @param databaseConnectionFinalUrl
	 * @return true/false whether it was verified or not
	 */
	private boolean verifyConnection(String connectionUsername, String connectionPassword,
		String databaseConnectionFinalUrl) {
		try {
			// verify connection
			//Set Database Driver using driver String
			Class.forName(loadedDriverString).newInstance();
			Connection tempConnection = DriverManager.getConnection(databaseConnectionFinalUrl, connectionUsername,
				connectionPassword);
			tempConnection.close();
			return true;
			
		}
		catch (Exception e) {
			errors.put("User account " + connectionUsername + " does not work. " + e.getMessage()
					+ " See the error log for more details",
				null); // TODO internationalize this
			log.warn("Error while checking the connection user account", e);
			return false;
		}
	}
	
	/**
	 * Convenience method to load the runtime properties file.
	 *
	 * @return the runtime properties file.
	 */
	private File getRuntimePropertiesFile() {
		File file;
		
		String pathName = OpenmrsUtil.getRuntimePropertiesFilePathName(WebConstants.WEBAPP_NAME);
		if (pathName != null) {
			file = new File(pathName);
		} else {
			file = new File(OpenmrsUtil.getApplicationDataDirectory(), getRuntimePropertiesFileName());
		}
		
		log.debug("Using file: " + file.getAbsolutePath());
		
		return file;
	}
	
	private String getRuntimePropertiesFileName() {
		String fileName = OpenmrsUtil.getRuntimePropertiesFileNameInTestMode();
		if (fileName == null) {
			fileName = WebConstants.WEBAPP_NAME + "-runtime.properties";
		}
		return fileName;
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#getTemplatePrefix()
	 */
	@Override
	protected String getTemplatePrefix() {
		return "org/openmrs/web/filter/initialization/";
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#getUpdateFilterModel()
	 */
	@Override
	protected Object getUpdateFilterModel() {
		return wizardModel;
	}
	
	/**
	 * @see org.openmrs.web.filter.StartupFilter#skipFilter(HttpServletRequest)
	 */
	@Override
	public boolean skipFilter(HttpServletRequest httpRequest) {
		// If progress.vm makes an ajax request even immediately after initialization has completed
		// let the request pass in order to let progress.vm load the start page of OpenMRS
		// (otherwise progress.vm is displayed "forever")
		return !PROGRESS_VM_AJAXREQUEST.equals(httpRequest.getParameter("page")) && !initializationRequired();
	}
	
	/**
	 * Public method that returns true if database+runtime properties initialization is required
	 *
	 * @return true if this initialization wizard needs to run
	 */
	public static boolean initializationRequired() {
		return !isInitializationComplete();
	}
	
	/**
	 * @param isInstallationStarted the value to set
	 */
	protected static synchronized void setInstallationStarted(boolean isInstallationStarted) {
		InitializationFilter.isInstallationStarted = isInstallationStarted;
	}
	
	/**
	 * @return true if installation has been started
	 */
	protected static boolean isInstallationStarted() {
		return isInstallationStarted;
	}
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		wizardModel = new InitializationWizardModel();
		DatabaseDetective databaseDetective = new DatabaseDetective();
		//set whether need to do initialization work
		if (databaseDetective.isDatabaseEmpty(OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME))) {
			//if runtime-properties file doesn't exist, have to do initialization work
			setInitializationComplete(false);
		} else {
			//if database is not empty, then let UpdaterFilter to judge whether need database update
			setInitializationComplete(true);
		}
	}
	
	private void importTestDataSet(InputStream in, String connectionUrl, String connectionUsername,
		String connectionPassword) throws IOException {
		File tempFile = null;
		FileOutputStream fileOut = null;
		try {
			ZipInputStream zipIn = new ZipInputStream(in);
			zipIn.getNextEntry();
			
			tempFile = File.createTempFile("testDataSet", "dump");
			fileOut = new FileOutputStream(tempFile);
			
			IOUtils.copy(zipIn, fileOut);
			
			fileOut.close();
			zipIn.close();
			
			//Cater for the stand-alone connection url with has :mxj:
			if (connectionUrl.contains(":mxj:")) {
				connectionUrl = connectionUrl.replace(":mxj:", ":");
			}
			
			URI uri = URI.create(connectionUrl.substring(5)); //remove 'jdbc:' prefix to conform to the URI format
			String host = uri.getHost();
			int port = uri.getPort();
			
			TestInstallUtil.addTestData(host, port, wizardModel.databaseName, connectionUsername, connectionPassword,
				tempFile.getAbsolutePath());
		}
		finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(fileOut);
			
			if (tempFile != null) {
				tempFile.delete();
			}
		}
	}
	
	private boolean isCurrentDatabase(String database) {
		return wizardModel.databaseConnection.contains(database);
	}
	
	/**
	 * @param silent if this statement fails do not display stack trace or record an error in the wizard
	 *            object.
	 * @param user username to connect with
	 * @param pw password to connect with
	 * @param sql String containing sql and question marks
	 * @param args the strings to fill into the question marks in the given sql
	 * @return result of executeUpdate or -1 for error
	 */
	private int executeStatement(boolean silent, String user, String pw, String sql, String... args) {
		
		Connection connection = null;
		Statement statement = null;
		try {
			String replacedSql = sql;
			
			// TODO how to get the driver for the other dbs...
			if (isCurrentDatabase(DATABASE_MYSQL)) {
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			} else if (isCurrentDatabase(DATABASE_POSTGRESQL)) {
				Class.forName("org.postgresql.Driver").newInstance();
				replacedSql = replacedSql.replaceAll("`", "\"");
			} else {
				replacedSql = replacedSql.replaceAll("`", "\"");
			}
			
			String tempDatabaseConnection;
			if (sql.contains("create database")) {
				tempDatabaseConnection = wizardModel.databaseConnection.replace("@DBNAME@",
					""); // make this dbname agnostic so we can create the db
			} else {
				tempDatabaseConnection = wizardModel.databaseConnection.replace("@DBNAME@", wizardModel.databaseName);
			}
			
			connection = DriverManager.getConnection(tempDatabaseConnection, user, pw);
			
			for (String arg : args) {
				arg = arg.replace(";", "&#094"); // to prevent any sql injection
				replacedSql = replacedSql.replaceFirst("\\?", arg);
			}
			
			// run the sql statement
			statement = connection.createStatement();
			
			return statement.executeUpdate(replacedSql);
			
		}
		catch (SQLException sqlex) {
			if (!silent) {
				// log and add error
				log.warn("error executing sql: " + sql, sqlex);
				errors.put("Error executing sql: " + sql + " - " + sqlex.getMessage(), null);
			}
		}
		catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
			log.error("Error generated", e);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException e) {
				log.warn("Error while closing statement");
			}
			try {
				
				if (connection != null) {
					connection.close();
				}
			}
			catch (Exception e) {
				log.warn("Error while closing connection", e);
			}
		}
		
		return -1;
	}
	
	/**
	 * Convenience variable to know if this wizard has completed successfully and that this wizard does
	 * not need to be executed again
	 *
	 * @return true if this has been run already
	 */
	private static synchronized boolean isInitializationComplete() {
		return initializationComplete;
	}
	
	/**
	 * Check if the given value is null or a zero-length String
	 *
	 * @param value the string to check
	 * @param errors the list of errors to append the errorMessage to if value is empty
	 * @param errorMessageCode the string with code of error message translation to append if value is
	 *            empty
	 * @return true if the value is non-empty
	 */
	private boolean checkForEmptyValue(String value, Map<String, Object[]> errors, String errorMessageCode) {
		if (!StringUtils.isEmpty(value)) {
			return true;
		}
		errors.put(errorMessageCode, null);
		return false;
	}
	
	/**
	 * Separate thread that will run through all tasks to complete the initialization. The database is
	 * created, user's created, etc here
	 */
	private class InitializationCompletion {
		
		private Thread thread;
		
		private int steps = 0;
		
		private String message = "";
		
		private Map<String, Object[]> errors = new HashMap<>();
		
		private String errorPage = null;
		
		private boolean erroneous = false;
		
		private int completedPercentage = 0;
		
		private WizardTask executingTask;
		
		private List<WizardTask> executedTasks = new ArrayList<>();
		
		public synchronized void reportError(String error, String errorPage, Object... params) {
			errors.put(error, params);
			this.errorPage = errorPage;
			erroneous = true;
		}
		
		public synchronized boolean hasErrors() {
			return erroneous;
		}
		
		public synchronized String getErrorPage() {
			return errorPage;
		}
		
		public synchronized Map<String, Object[]> getErrors() {
			return errors;
		}
		
		/**
		 * Start the completion stage. This fires up the thread to do all the work.
		 */
		public void start() {
			setStepsComplete(0);
			setInitializationComplete(false);
			thread.start();
		}
		
		public void waitForCompletion() {
			try {
				thread.join();
			}
			catch (InterruptedException e) {
				log.error("Error generated", e);
			}
		}
		
		protected synchronized void setStepsComplete(int steps) {
			this.steps = steps;
		}
		
		protected synchronized int getStepsComplete() {
			return steps;
		}
		
		public synchronized String getMessage() {
			return message;
		}
		
		public synchronized void setMessage(String message) {
			this.message = message;
			setStepsComplete(getStepsComplete() + 1);
		}
		
		/**
		 * @return the executingTask
		 */
		protected synchronized WizardTask getExecutingTask() {
			return executingTask;
		}
		
		/**
		 * @return the completedPercentage
		 */
		protected synchronized int getCompletedPercentage() {
			return completedPercentage;
		}
		
		/**
		 * @param completedPercentage the completedPercentage to set
		 */
		protected synchronized void setCompletedPercentage(int completedPercentage) {
			this.completedPercentage = completedPercentage;
		}
		
		/**
		 * Adds a task that has been completed to the list of executed tasks
		 *
		 * @param task
		 */
		protected synchronized void addExecutedTask(WizardTask task) {
			this.executedTasks.add(task);
		}
		
		/**
		 * @param executingTask the executingTask to set
		 */
		protected synchronized void setExecutingTask(WizardTask executingTask) {
			this.executingTask = executingTask;
		}
		
		/**
		 * @return the executedTasks
		 */
		protected synchronized List<WizardTask> getExecutedTasks() {
			return this.executedTasks;
		}
		
		/**
		 * This class does all the work of creating the desired database, user, updates, etc
		 */
		public InitializationCompletion() {
			Runnable r = new Runnable() {
				
				/**
				 * TODO split this up into multiple testable methods
				 *
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					try {
						String connectionUsername;
						StringBuilder connectionPassword = new StringBuilder();
						ChangeLogDetective changeLogDetective = new ChangeLogDetective();
						ChangeLogVersionFinder changeLogVersionFinder = new ChangeLogVersionFinder();
						
						if (!wizardModel.hasCurrentOpenmrsDatabase) {
							setMessage("Create database");
							setExecutingTask(WizardTask.CREATE_SCHEMA);
							// connect via jdbc and create a database
							String sql;
							if (isCurrentDatabase(DATABASE_MYSQL)) {
								sql = "create database if not exists `?` default character set utf8";
							} else if (isCurrentDatabase(DATABASE_POSTGRESQL)) {
								sql = "create database `?` encoding 'utf8'";
							} else if (isCurrentDatabase(DATABASE_H2)) {
								sql = null;
							} else {
								sql = "create database `?`";
							}
							
							int result;
							if (sql != null) {
								result = executeStatement(false, wizardModel.createDatabaseUsername,
									wizardModel.createDatabasePassword, sql, wizardModel.databaseName);
							} else {
								result = 1;
							}
							// throw the user back to the main screen if this error occurs
							if (result < 0) {
								reportError(ErrorMessageConstants.ERROR_DB_CREATE_NEW, DEFAULT_PAGE);
								return;
							} else {
								wizardModel.workLog.add("Created database " + wizardModel.databaseName);
							}
							
							addExecutedTask(WizardTask.CREATE_SCHEMA);
						}
						
						if (wizardModel.createDatabaseUser) {
							setMessage("Create database user");
							setExecutingTask(WizardTask.CREATE_DB_USER);
							connectionUsername = wizardModel.databaseName + "_user";
							if (connectionUsername.length() > 16) {
								connectionUsername = wizardModel.databaseName.substring(0, 11)
									+ "_user"; // trim off enough to leave space for _user at the end
							}
							
							connectionPassword.append("");
							// generate random password from this subset of alphabet
							// intentionally left out these characters: ufsb$() to prevent certain words forming randomly
							String chars = "acdeghijklmnopqrtvwxyzACDEGHIJKLMNOPQRTVWXYZ0123456789.|~@#^&";
							Random r = new Random();
							StringBuilder randomStr = new StringBuilder("");
							for (int x = 0; x < 12; x++) {
								randomStr.append(chars.charAt(r.nextInt(chars.length())));
							}
							connectionPassword.append(randomStr);
							
							// connect via jdbc with root user and create an openmrs user
							String host = "'%'";
							if (wizardModel.databaseConnection.contains("localhost")
								|| wizardModel.databaseConnection.contains("127.0.0.1")) {
								host = "'localhost'";
							}
							
							String sql = "";
							if (isCurrentDatabase(DATABASE_MYSQL)) {
								sql = "drop user '?'@" + host;
							} else if (isCurrentDatabase(DATABASE_POSTGRESQL)) {
								sql = "drop user `?`";
							}
							
							executeStatement(true, wizardModel.createUserUsername, wizardModel.createUserPassword, sql,
								connectionUsername);
							
							if (isCurrentDatabase(DATABASE_MYSQL)) {
								sql = "create user '?'@" + host + " identified by '?'";
							} else if (isCurrentDatabase(DATABASE_POSTGRESQL)) {
								sql = "create user `?` with password '?'";
							}
							
							if (-1 != executeStatement(false, wizardModel.createUserUsername, wizardModel.createUserPassword,
								sql, connectionUsername, connectionPassword.toString())) {
								wizardModel.workLog.add("Created user " + connectionUsername);
							} else {
								// if error occurs stop
								reportError(ErrorMessageConstants.ERROR_DB_CREATE_DB_USER, DEFAULT_PAGE);
								return;
							}
							
							// grant the roles
							int result = 1;
							if (isCurrentDatabase(DATABASE_MYSQL)) {
								sql = "GRANT ALL ON `?`.* TO '?'@" + host;
								result = executeStatement(false, wizardModel.createUserUsername,
									wizardModel.createUserPassword, sql, wizardModel.databaseName, connectionUsername);
							} else if (isCurrentDatabase(DATABASE_POSTGRESQL)) {
								sql = "ALTER USER `?` WITH SUPERUSER";
								result = executeStatement(false, wizardModel.createUserUsername,
									wizardModel.createUserPassword, sql, connectionUsername);
							}
							
							// throw the user back to the main screen if this error occurs
							if (result < 0) {
								reportError(ErrorMessageConstants.ERROR_DB_GRANT_PRIV, DEFAULT_PAGE);
								return;
							} else {
								wizardModel.workLog.add("Granted user " + connectionUsername + " all privileges to database "
									+ wizardModel.databaseName);
							}
							
							addExecutedTask(WizardTask.CREATE_DB_USER);
						} else {
							connectionUsername = wizardModel.currentDatabaseUsername;
							connectionPassword.setLength(0);
							connectionPassword.append(wizardModel.currentDatabasePassword);
						}
						
						String finalDatabaseConnectionString = wizardModel.databaseConnection.replace("@DBNAME@",
							wizardModel.databaseName);
						
						finalDatabaseConnectionString = finalDatabaseConnectionString.replace("@APPLICATIONDATADIR@",
							OpenmrsUtil.getApplicationDataDirectory().replace("\\", "/"));
						
						// verify that the database connection works
						if (!verifyConnection(connectionUsername, connectionPassword.toString(),
							finalDatabaseConnectionString)) {
							setMessage("Verify that the database connection works");
							// redirect to setup page if we got an error
							reportError("Unable to connect to database", DEFAULT_PAGE);
							return;
						}
						
						// save the properties for startup purposes
						Properties runtimeProperties = new Properties();
						
						runtimeProperties.put("connection.url", finalDatabaseConnectionString);
						runtimeProperties.put("connection.username", connectionUsername);
						runtimeProperties.put("connection.password", connectionPassword.toString());
						if (StringUtils.hasText(wizardModel.databaseDriver)) {
							runtimeProperties.put("connection.driver_class", wizardModel.databaseDriver);
						}
						if (finalDatabaseConnectionString.contains(DATABASE_POSTGRESQL)) {
							runtimeProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
						}
						if (finalDatabaseConnectionString.contains(DATABASE_SQLSERVER)) {
							runtimeProperties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
						}
						if (finalDatabaseConnectionString.contains(DATABASE_H2)) {
							runtimeProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
						}
						runtimeProperties.put("module.allow_web_admin", wizardModel.moduleWebAdmin.toString());
						runtimeProperties.put("auto_update_database", wizardModel.autoUpdateDatabase.toString());
						final Encoder base64 = Base64.getEncoder();
						runtimeProperties.put(OpenmrsConstants.ENCRYPTION_VECTOR_RUNTIME_PROPERTY,
							new String(base64.encode(Security.generateNewInitVector()), StandardCharsets.UTF_8));
						runtimeProperties.put(OpenmrsConstants.ENCRYPTION_KEY_RUNTIME_PROPERTY,
							new String(base64.encode(Security.generateNewSecretKey()), StandardCharsets.UTF_8));
						
						Properties properties = Context.getRuntimeProperties();
						properties.putAll(runtimeProperties);
						runtimeProperties = properties;
						Context.setRuntimeProperties(runtimeProperties);
						
						/**
						 * A callback class that prints out info about liquibase changesets
						 */
						class PrintingChangeSetExecutorCallback implements ChangeSetExecutorCallback {
							
							private int i = 1;
							
							private String message;
							
							public PrintingChangeSetExecutorCallback(String message) {
								this.message = message;
							}
							
							/**
							 * @see ChangeSetExecutorCallback#executing(liquibase.changelog.ChangeSet, int)
							 */
							@Override
							public void executing(ChangeSet changeSet, int numChangeSetsToRun) {
								setMessage(message + " (" + i++ + "/" + numChangeSetsToRun + "): Author: "
									+ changeSet.getAuthor() + " Comments: " + changeSet.getComments() + " Description: "
									+ changeSet.getDescription());
								float numChangeSetsToRunFloat = (float) numChangeSetsToRun;
								float j = (float) i;
								setCompletedPercentage(Math.round(j * 100 / numChangeSetsToRunFloat));
							}
							
						}
						
						if (wizardModel.createTables) {
							// use liquibase to create core data + tables
							try {
								String liquibaseSchemaFileName = changeLogVersionFinder.getLatestSchemaSnapshotFilename()
									.get();
								String liquibaseCoreDataFileName = changeLogVersionFinder.getLatestCoreDataSnapshotFilename()
									.get();
								
								setMessage("Executing " + liquibaseSchemaFileName);
								setExecutingTask(WizardTask.CREATE_TABLES);
								
								log.debug("executing Liquibase file '{}' ", liquibaseSchemaFileName);
								
								DatabaseUpdater.executeChangelog(liquibaseSchemaFileName,
									new PrintingChangeSetExecutorCallback("OpenMRS schema file"));
								addExecutedTask(WizardTask.CREATE_TABLES);
								
								//reset for this task
								setCompletedPercentage(0);
								setExecutingTask(WizardTask.ADD_CORE_DATA);
								
								log.debug("executing Liquibase file '{}' ", liquibaseCoreDataFileName);
								
								DatabaseUpdater.executeChangelog(liquibaseCoreDataFileName,
									new PrintingChangeSetExecutorCallback("OpenMRS core data file"));
								wizardModel.workLog.add("Created database tables and added core data");
								addExecutedTask(WizardTask.ADD_CORE_DATA);
								
							}
							catch (Exception e) {
								reportError(ErrorMessageConstants.ERROR_DB_CREATE_TABLES_OR_ADD_DEMO_DATA, DEFAULT_PAGE,
									e.getMessage());
								log.warn("Error while trying to create tables and demo data", e);
							}
						}
						
						if (wizardModel.importTestData) {
							try {
								setMessage("Importing test data");
								setExecutingTask(WizardTask.IMPORT_TEST_DATA);
								setCompletedPercentage(0);
								
								try {
									InputStream inData = TestInstallUtil.getResourceInputStream(
										wizardModel.remoteUrl + RELEASE_TESTING_MODULE_PATH + "generateTestDataSet.form",
										wizardModel.remoteUsername, wizardModel.remotePassword);
									
									setCompletedPercentage(40);
									setMessage("Loading imported test data...");
									importTestDataSet(inData, finalDatabaseConnectionString, connectionUsername,
										connectionPassword.toString());
									wizardModel.workLog.add("Imported test data");
									addExecutedTask(WizardTask.IMPORT_TEST_DATA);
									
									//reset the progress for the next task
									setCompletedPercentage(0);
									setMessage("Importing modules from remote server...");
									setExecutingTask(WizardTask.ADD_MODULES);
									
									InputStream inModules = TestInstallUtil.getResourceInputStream(
										wizardModel.remoteUrl + RELEASE_TESTING_MODULE_PATH + "getModules.htm",
										wizardModel.remoteUsername, wizardModel.remotePassword);
									
									setCompletedPercentage(90);
									setMessage("Adding imported modules...");
									if (!TestInstallUtil.addZippedTestModules(inModules)) {
										reportError(ErrorMessageConstants.ERROR_DB_UNABLE_TO_ADD_MODULES, DEFAULT_PAGE, "");
										return;
									} else {
										wizardModel.workLog.add("Added Modules");
										addExecutedTask(WizardTask.ADD_MODULES);
									}
								}
								catch (APIAuthenticationException e) {
									log.warn("Unable to authenticate as a User with the System Developer role");
									reportError(ErrorMessageConstants.UPDATE_ERROR_UNABLE_AUTHENTICATE,
										TESTING_REMOTE_DETAILS_SETUP, "");
									return;
								}
							}
							catch (Exception e) {
								reportError(ErrorMessageConstants.ERROR_DB_IMPORT_TEST_DATA, DEFAULT_PAGE, e.getMessage());
								log.warn("Error while trying to import test data", e);
								return;
							}
						}
						
						// add demo data only if creating tables fresh and user selected the option add demo data
						if (wizardModel.createTables && wizardModel.addDemoData) {
							try {
								setMessage("Adding demo data");
								setCompletedPercentage(0);
								setExecutingTask(WizardTask.ADD_DEMO_DATA);
								
								log.debug("executing Liquibase file '{}' ", LIQUIBASE_DEMO_DATA);
								
								DatabaseUpdater.executeChangelog(LIQUIBASE_DEMO_DATA,
									new PrintingChangeSetExecutorCallback("OpenMRS demo patients, users, and forms"));
								wizardModel.workLog.add("Added demo data");
								
								addExecutedTask(WizardTask.ADD_DEMO_DATA);
							}
							catch (Exception e) {
								reportError(ErrorMessageConstants.ERROR_DB_CREATE_TABLES_OR_ADD_DEMO_DATA, DEFAULT_PAGE,
									e.getMessage());
								log.warn("Error while trying to add demo data", e);
							}
						}
						
						// update the database to the latest version
						try {
							setMessage("Updating the database to the latest version");
							setCompletedPercentage(0);
							setExecutingTask(WizardTask.UPDATE_TO_LATEST);
							
							String version = null;
							
							if (wizardModel.createTables) {
								version = changeLogVersionFinder.getLatestSnapshotVersion().get();
							} else {
								version = changeLogDetective.getInitialLiquibaseSnapshotVersion(DatabaseUpdater.CONTEXT,
									new DatabaseUpdaterLiquibaseProvider());
							}
							
							log.debug(
								"updating the database with versions of liquibase-update-to-latest files greater than '{}'",
								version);
							
							List<String> changelogs = changeLogVersionFinder
								.getUpdateFileNames(changeLogVersionFinder.getUpdateVersionsGreaterThan(version));
							
							for (String changelog : changelogs) {
								log.debug("applying Liquibase changelog '{}'", changelog);
								
								DatabaseUpdater.executeChangelog(changelog,
									new PrintingChangeSetExecutorCallback("executing Liquibase changelog " + changelog));
							}
							addExecutedTask(WizardTask.UPDATE_TO_LATEST);
						}
						catch (Exception e) {
							reportError(ErrorMessageConstants.ERROR_DB_UPDATE_TO_LATEST, DEFAULT_PAGE, e.getMessage());
							log.warn("Error while trying to update to the latest database version", e);
							return;
						}
						
						setExecutingTask(null);
						setMessage("Starting OpenMRS");
						
						// start spring
						// after this point, all errors need to also call: contextLoader.closeWebApplicationContext(event.getServletContext())
						// logic copied from org.springframework.web.context.ContextLoaderListener
						ContextLoader contextLoader = new ContextLoader();
						contextLoader.initWebApplicationContext(filterConfig.getServletContext());
						
						// output properties to the openmrs runtime properties file so that this wizard is not run again
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(getRuntimePropertiesFile());
							OpenmrsUtil.storeProperties(runtimeProperties, fos,
								"Auto generated by OpenMRS initialization wizard");
							wizardModel.workLog.add("Saved runtime properties file " + getRuntimePropertiesFile());
							
							/*
							 * Fix file readability permissions:
							 * first revoke read permission from everyone, then set read permissions for only the user
							 * there is no function to set specific readability for only one user
							 * and revoke everyone else's, therefore this is the only way to accomplish this.
							 */
							wizardModel.workLog.add("Adjusting file posix properties to user readonly");
							if (getRuntimePropertiesFile().setReadable(false, false)
								&& getRuntimePropertiesFile().setReadable(true)) {
								wizardModel.workLog
									.add("Successfully adjusted RuntimePropertiesFile to disallow world to read it");
							} else {
								wizardModel.workLog
									.add("Unable to adjust RuntimePropertiesFile to disallow world to read it");
							}
							// don't need to catch errors here because we tested it at the beginning of the wizard
						}
						finally {
							if (fos != null) {
								fos.close();
							}
						}
						
						Context.openSession();
						
						if (!"".equals(wizardModel.implementationId)) {
							try {
								Context.addProxyPrivilege(PrivilegeConstants.MANAGE_GLOBAL_PROPERTIES);
								Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPT_SOURCES);
								Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_SOURCES);
								Context.addProxyPrivilege(PrivilegeConstants.MANAGE_IMPLEMENTATION_ID);
								
								ImplementationId implId = new ImplementationId();
								implId.setName(wizardModel.implementationIdName);
								implId.setImplementationId(wizardModel.implementationId);
								implId.setPassphrase(wizardModel.implementationIdPassPhrase);
								implId.setDescription(wizardModel.implementationIdDescription);
								
								Context.getAdministrationService().setImplementationId(implId);
							}
							catch (Exception e) {
								reportError(ErrorMessageConstants.ERROR_SET_INPL_ID, DEFAULT_PAGE, e.getMessage());
								log.warn("Implementation ID could not be set.", e);
								Context.shutdown();
								WebModuleUtil.shutdownModules(filterConfig.getServletContext());
								contextLoader.closeWebApplicationContext(filterConfig.getServletContext());
								return;
							}
							finally {
								Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_GLOBAL_PROPERTIES);
								Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPT_SOURCES);
								Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_SOURCES);
								Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_IMPLEMENTATION_ID);
							}
						}
						
						try {
							// change the admin user password from "test" to what they input above
							if (wizardModel.createTables) {
								try {
									Context.authenticate(new UsernamePasswordCredentials("admin", "test"));
									
									Properties props = Context.getRuntimeProperties();
									String initValue = props.getProperty(UserService.ADMIN_PASSWORD_LOCKED_PROPERTY);
									props.setProperty(UserService.ADMIN_PASSWORD_LOCKED_PROPERTY, "false");
									Context.setRuntimeProperties(props);
									
									Context.getUserService().changePassword("test", wizardModel.adminUserPassword);
									
									if (initValue == null) {
										props.remove(UserService.ADMIN_PASSWORD_LOCKED_PROPERTY);
									} else {
										props.setProperty(UserService.ADMIN_PASSWORD_LOCKED_PROPERTY, initValue);
									}
									Context.setRuntimeProperties(props);
									Context.logout();
								}
								catch (ContextAuthenticationException ex) {
									log.info("No need to change admin password.", ex);
								}
							}
						}
						catch (Exception e) {
							Context.shutdown();
							WebModuleUtil.shutdownModules(filterConfig.getServletContext());
							contextLoader.closeWebApplicationContext(filterConfig.getServletContext());
							reportError(ErrorMessageConstants.ERROR_COMPLETE_STARTUP, DEFAULT_PAGE, e.getMessage());
							log.warn("Unable to complete the startup.", e);
							return;
						}
						
						try {
							// Update PostgreSQL Sequences after insertion of core data
							Context.getAdministrationService().updatePostgresSequence();
						}
						catch (Exception e) {
							log.warn("Not able to update PostgreSQL sequence. Startup failed for PostgreSQL", e);
							reportError(ErrorMessageConstants.ERROR_COMPLETE_STARTUP, DEFAULT_PAGE, e.getMessage());
							return;
						}
						
						// set this so that the wizard isn't run again on next page load
						Context.closeSession();
						
						// start openmrs
						try {
							UpdateFilter.setUpdatesRequired(false);
							WebDaemon.startOpenmrs(filterConfig.getServletContext());
						}
						catch (DatabaseUpdateException updateEx) {
							log.warn("Error while running the database update file", updateEx);
							reportError(ErrorMessageConstants.ERROR_DB_UPDATE, DEFAULT_PAGE, updateEx.getMessage());
							return;
						}
						catch (InputRequiredException inputRequiredEx) {
							// TODO display a page looping over the required input and ask the user for each.
							// 		When done and the user and put in their say, call DatabaseUpdater.update(Map);
							//		with the user's question/answer pairs
							log.warn(
								"Unable to continue because user input is required for the db updates and we cannot do anything about that right now");
							reportError(ErrorMessageConstants.ERROR_INPUT_REQ, DEFAULT_PAGE);
							return;
						}
						catch (MandatoryModuleException mandatoryModEx) {
							log.warn(
								"A mandatory module failed to start. Fix the error or unmark it as mandatory to continue.",
								mandatoryModEx);
							reportError(ErrorMessageConstants.ERROR_MANDATORY_MOD_REQ, DEFAULT_PAGE,
								mandatoryModEx.getMessage());
							return;
						}
						catch (OpenmrsCoreModuleException coreModEx) {
							log.warn(
								"A core module failed to start. Make sure that all core modules (with the required minimum versions) are installed and starting properly.",
								coreModEx);
							reportError(ErrorMessageConstants.ERROR_CORE_MOD_REQ, DEFAULT_PAGE, coreModEx.getMessage());
							return;
						}
						
						// TODO catch openmrs errors here and drop the user back out to the setup screen
						
					}
					catch (IOException e) {
						reportError(ErrorMessageConstants.ERROR_COMPLETE_STARTUP, DEFAULT_PAGE, e.getMessage());
					}
					finally {
						if (!hasErrors()) {
							// set this so that the wizard isn't run again on next page load
							setInitializationComplete(true);
							// we should also try to store selected by user language
							// if user wants to system will do it for him 
							FilterUtil.storeLocale(wizardModel.localeToSave);
						}
						setInstallationStarted(false);
					}
				}
			};
			
			thread = new Thread(r);
		}
	}
	
	/**
	 * Convenience method that loads the database driver
	 *
	 * @param connection the database connection string
	 * @param databaseDriver the database driver class name to load
	 * @return the loaded driver string
	 */
	public static String loadDriver(String connection, String databaseDriver) {
		String loadedDriverString = null;
		try {
			loadedDriverString = DatabaseUtil.loadDatabaseDriver(connection, databaseDriver);
			log.info("using database driver :" + loadedDriverString);
		}
		catch (ClassNotFoundException e) {
			log.error("The given database driver class was not found. "
				+ "Please ensure that the database driver jar file is on the class path "
				+ "(like in the webapp's lib folder)");
		}
		
		return loadedDriverString;
	}
	
	/**
	 * Utility method that checks if there is a runtime properties file containing database connection
	 * credentials
	 *
	 * @return
	 */
	private static boolean skipDatabaseSetupPage() {
		Properties props = OpenmrsUtil.getRuntimeProperties(WebConstants.WEBAPP_NAME);
		return (props != null && StringUtils.hasText(props.getProperty("connection.url"))
			&& StringUtils.hasText(props.getProperty("connection.username"))
			&& StringUtils.hasText(props.getProperty("connection.password")));
	}
	
	/**
	 * Utility methods that checks if the user clicked the back image
	 *
	 * @param httpRequest
	 * @return
	 */
	private static boolean goBack(HttpServletRequest httpRequest) {
		return "Back".equals(httpRequest.getParameter("back"))
			|| (httpRequest.getParameter("back.x") != null && httpRequest.getParameter("back.y") != null);
	}
	
	/**
	 * Convenience method to get custom installation script
	 *
	 * @return Properties from custom installation script or empty if none specified
	 * @throws RuntimeException if path to installation script is invalid
	 */
	private Properties getInstallationScript() {
		Properties prop = new Properties();
		
		String fileName = System.getProperty("OPENMRS_INSTALLATION_SCRIPT");
		if (fileName == null) {
			return prop;
		}
		if (fileName.startsWith("classpath:")) {
			fileName = fileName.substring(10);
			InputStream input = null;
			try {
				input = getClass().getClassLoader().getResourceAsStream(fileName);
				prop.load(input);
				log.info("Using installation script from classpath: " + fileName);
				
				input.close();
			}
			catch (IOException ex) {
				log.error("Failed to load installation script from classpath: " + fileName, ex);
				throw new RuntimeException(ex);
			}
			finally {
				IOUtils.closeQuietly(input);
			}
		} else {
			File file = new File(fileName);
			if (file.exists()) {
				InputStream input = null;
				try {
					input = new FileInputStream(fileName);
					prop.load(input);
					log.info("Using installation script from absolute path: " + file.getAbsolutePath());
					
					input.close();
				}
				catch (IOException ex) {
					log.error("Failed to load installation script from absolute path: " + file.getAbsolutePath(), ex);
					throw new RuntimeException(ex);
				}
				finally {
					IOUtils.closeQuietly(input);
				}
			}
		}
		return prop;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/ModuleServlet.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1239820102030303L;
	
	private static final Logger log = LoggerFactory.getLogger(ModuleServlet.class);
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("In service method for module servlet: " + request.getPathInfo());
		String servletName = request.getPathInfo();
		int end = servletName.indexOf("/", 1);
		
		String moduleId = null;
		if (end > 0) {
			moduleId = servletName.substring(1, end);
		}
		
		log.debug("ModuleId: " + moduleId);
		Module mod = ModuleFactory.getModuleById(moduleId);
		 
		// where in the path to start trimming
		int start = 1;
		if (mod != null) {
			log.debug("Module with id " + moduleId + " found.  Looking for servlet name after " + moduleId + " in url path");
			start = moduleId.length() + 2;
			// this skips over the moduleId that is in the path
		}
		
		end = servletName.indexOf("/", start);
		if (end == -1 || end > servletName.length()) {
			end = servletName.length();
		}
		servletName = servletName.substring(start, end);
		
		log.debug("Servlet name: " + servletName);
		
		HttpServlet servlet = WebModuleUtil.getServlet(servletName);
		
		if (servlet == null) {
			log.warn("No servlet with name: " + servletName + " was found");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		servlet.service(request, response);
	}

	/**
	 * Internal implementation of the ServletConfig interface, to be passed to module servlets when
	 * they are first loaded
	 */
	public static class SimpleServletConfig implements ServletConfig {
		
		private String name;
		
		private ServletContext servletContext;

		private final Map<String, String> initParameters;

		public SimpleServletConfig(String name, ServletContext servletContext, Map<String, String> initParameters) {
			this.name = name;
			this.servletContext = servletContext;
			this.initParameters = initParameters;
		}
		
		@Override
		public String getServletName() {
			return name;
		}
		
		@Override
		public ServletContext getServletContext() {
			return servletContext;
		}
		
		// not implemented in a module's config.xml yet
		@Override
		public String getInitParameter(String paramName) {
			return initParameters.get(paramName);
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return Collections.enumeration(initParameters.keySet());
		}
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/ModuleResourcesServlet.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.module.Module;
import org.openmrs.module.ModuleUtil;
import org.openmrs.util.OpenmrsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleResourcesServlet extends HttpServlet {
	
	private static final String MODULE_PATH = "/WEB-INF/view/module/";
	
	private static final long serialVersionUID = 1239820102030344L;
	
	private static final Logger log = LoggerFactory.getLogger(ModuleResourcesServlet.class);
	
	/**
	 * Used for caching purposes
	 *
	 * @see javax.servlet.http.HttpServlet#getLastModified(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected long getLastModified(HttpServletRequest req) {
		File f = getFile(req);
		
		if (f == null) {
			return super.getLastModified(req);
		}
		
		return f.lastModified();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.debug("In service method for module servlet: " + request.getPathInfo());
		
		File f = getFile(request);
		if (f == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		response.setDateHeader("Last-Modified", f.lastModified());
		response.setContentLength(Long.valueOf(f.length()).intValue());
		String mimeType = getServletContext().getMimeType(f.getName());
		response.setContentType(mimeType);
		
		FileInputStream is = new FileInputStream(f);
		try {
			OpenmrsUtil.copyFile(is, response.getOutputStream());
		}
		finally {
			OpenmrsUtil.closeStream(is);
		}
	}
	
	/**
	 * Turns the given request/path into a File object
	 *
	 * @param request the current http request
	 * @return the file being requested or null if not found
	 */
	protected File getFile(HttpServletRequest request) {
		
		String path = request.getPathInfo();
		
		Module module = ModuleUtil.getModuleForPath(path);
		if (module == null) {
			log.warn("No module handles the path: " + path);
			return null;
		}
		
		String relativePath = ModuleUtil.getPathForResource(module, path);
		String realPath = getServletContext().getRealPath("") + MODULE_PATH + module.getModuleIdAsPath() + "/resources"
		        + relativePath;
		
		//if in dev mode, load resources from the development directory
		File devDir = ModuleUtil.getDevelopmentDirectory(module.getModuleId());
		if (devDir != null) {
			realPath = devDir.getAbsolutePath() + "/omod/target/classes/web/module/resources" + relativePath;
		}
		
		realPath = realPath.replace("/", File.separator);
		
		File f = new File(realPath);
		if (!f.exists()) {
			log.warn("No file with path '" + realPath + "' exists for module '" + module.getModuleId() + "'");
			return null;
		}
		
		return f;
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/OpenmrsJspServlet.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.jasper.Constants;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.TldCache;
import org.apache.jasper.servlet.JspServlet;
import org.apache.jasper.servlet.TldScanner;
import org.openmrs.util.OpenmrsClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The purpose of this class is to provide a custom JspServlet implementation that accounts for TLD files introduced
 * by OpenMRS modules.  From Tomcat 8 onward, a change to the Tomcat TLD processing necessitates this step.
 * See:  https://issues.openmrs.org/browse/LUI-169
 */
public class OpenmrsJspServlet extends JspServlet {
	
	private static final Logger log = LoggerFactory.getLogger(OpenmrsJspServlet.class);
	
	public static final String OPENMRS_TLD_SCAN_NEEDED = "OPENMRS_TLD_SCAN_NEEDED";

	@Override
	public void init(ServletConfig config) throws ServletException {
		Thread.currentThread().setContextClassLoader(OpenmrsClassLoader.getInstance());
		super.init(config);
	}
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		rescanTldsIfNeeded();
		super.service(request, response);
	}

	protected synchronized void rescanTldsIfNeeded() throws ServletException {
		if (getBooleanAttribute(OPENMRS_TLD_SCAN_NEEDED, true)) {
			log.warn("Rescanning TLDs");
			boolean namespaceAware = true;
			boolean validate = getBooleanParameter(Constants.XML_VALIDATION_TLD_INIT_PARAM, false);
			boolean blockExternalString = getBooleanParameter(Constants.XML_BLOCK_EXTERNAL_INIT_PARAM, true);
			try {
				TldScanner scanner = new TldScanner(getServletContext(), namespaceAware, validate, blockExternalString);
				try {
					scanner.scan();
				} catch (IOException | SAXException e) {
					throw new ServletException(e);
				}
				// add any listeners defined in TLDs
				for (String listener : scanner.getListeners()) {
					getServletContext().addListener(listener);
				}

				TldCache tldCache = new TldCache(getServletContext(), scanner.getUriTldResourcePathMap(), scanner.getTldResourcePathTaglibXmlMap());
				getServletContext().setAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME, tldCache);
				log.info("TldCache updated on ServletContext");
				try {
					Options options = (Options) FieldUtils.readField(this, "options", true);
					if (options instanceof EmbeddedServletOptions) {
						EmbeddedServletOptions embeddedServletOptions = (EmbeddedServletOptions) options;
						embeddedServletOptions.setTldCache(tldCache);
						log.info("TldCache updated on JspServlet");
					}
				} catch (IllegalAccessException e) {
					throw new ServletException("Unable to set TldCache on JspServlet options", e);
				}
			} catch (NoClassDefFoundError e) {
				/*
					If we hit a NoClassDefFoundError, assume this means that we are operating in a Non-Tomcat
					environment, or we are in a version of Tomcat 7 or before, which does not require this additional
					TLD Scanning Steps.  Proceed with startup.
				 */
				log.debug("Got NoClassDefFound error, skipping additional TLD scanning step");
			} finally {
				log.info("Scanning completed successfully");
				getServletContext().setAttribute(OPENMRS_TLD_SCAN_NEEDED, false);
			}
		}
	}

	private boolean getBooleanParameter(String parameter, boolean defaultValue) {
		String val = getServletContext().getInitParameter(parameter);
		if (StringUtils.isNotBlank(val)) {
			return Boolean.parseBoolean(val);
		}
		return defaultValue;
	}

	private boolean getBooleanAttribute(String attribute, boolean defaultValue) {
		Boolean val = (Boolean)getServletContext().getAttribute(attribute);
		if (val != null) {
			return val;
		}
		return defaultValue;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/WebModuleUtil.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleException;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.ModuleUtil;
import org.openmrs.module.web.filter.ModuleFilterConfig;
import org.openmrs.module.web.filter.ModuleFilterDefinition;
import org.openmrs.module.web.filter.ModuleFilterMapping;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;
import org.openmrs.web.DispatcherServlet;
import org.openmrs.web.StaticDispatcherServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class WebModuleUtil {

	private WebModuleUtil() {
	}
	
	private static final Logger log = LoggerFactory.getLogger(WebModuleUtil.class);
	
	private static final Lock SERVLET_LOCK = new ReentrantLock();
	
	private static final Lock FILTERS_LOCK = new ReentrantLock();
	
	// caches all modules' mapped servlets
	private static final Map<String, HttpServlet> MODULE_SERVLETS = new HashMap<>();
	
	// caches all modules filters and filter-mappings
	private static final Map<Module, Collection<Filter>> MODULE_FILTERS = new HashMap<>();
	
	private static final Map<String, Filter> MODULE_FILTERS_BY_NAME = new HashMap<>();
	
	private static final Deque<ModuleFilterMapping> MODULE_FILTER_MAPPINGS = new ArrayDeque<>();
	
	private static DispatcherServlet dispatcherServlet = null;
	
	private static StaticDispatcherServlet staticDispatcherServlet = null;
	
	/**
	 * Performs the webapp specific startup needs for modules Normal startup is done in
	 * {@link ModuleFactory#startModule(Module)} If delayContextRefresh is true, the spring context
	 * is not rerun. This will save a lot of time, but it also means that the calling method is
	 * responsible for restarting the context if necessary (the calling method will also have to
	 * call {@link #loadServlets(Module, ServletContext)} and
	 * {@link #loadFilters(Module, ServletContext)}).<br>
	 * <br>
	 * If delayContextRefresh is true and this module should have caused a context refresh, a true
	 * value is returned. Otherwise, false is returned
	 *
	 * @param mod Module to start
	 * @param servletContext the current ServletContext
	 * @param delayContextRefresh true/false whether or not to do the context refresh
	 * @return boolean whether or not the spring context need to be refreshed
	 */
	public static boolean startModule(Module mod, ServletContext servletContext, boolean delayContextRefresh) {
		
		log.debug("trying to start module {}", mod);
		
		// only try and start this module if the api started it without a
		// problem.
		if (ModuleFactory.isModuleStarted(mod) && !mod.hasStartupError()) {
			
			String realPath = getRealPath(servletContext);
			
			if (realPath == null) {
				realPath = System.getProperty("user.dir");
			}
			
			File webInf = new File(realPath + "/WEB-INF".replace("/", File.separator));
			if (!webInf.exists()) {
				webInf.mkdir();
			}
			
			// flag to tell whether we added any xml/dwr/etc changes that necessitate a refresh
			// of the web application context
			boolean moduleNeedsContextRefresh = false;
			
			// copy the html files into the webapp (from /web/module/ in the module)
			// also looks for a spring context file. If found, schedules spring to be restarted
			JarFile jarFile = null;
			OutputStream outStream = null;
			InputStream inStream = null;
			try {
				File modFile = mod.getFile();
				jarFile = new JarFile(modFile);
				Enumeration<JarEntry> entries = jarFile.entries();
				
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					if (Paths.get(name).startsWith("..")) {
						throw new UnsupportedOperationException("Attempted to write file '" + name + "' rejected as it attempts to write outside the chosen directory. This may be the result of a zip-slip style attack.");
					}
					
					log.debug("Entry name: {}", name);
					if (name.startsWith("web/module/")) {
						// trim out the starting path of "web/module/"
						String filepath = name.substring(11);
						
						StringBuilder absPath = new StringBuilder(realPath + "/WEB-INF");
						
						// If this is within the tag file directory, copy it into /WEB-INF/tags/module/moduleId/...
						if (filepath.startsWith("tags/")) {
							filepath = filepath.substring(5);
							absPath.append("/tags/module/");
						}
						// Otherwise, copy it into /WEB-INF/view/module/moduleId/...
						else {
							absPath.append("/view/module/");
						}
						
						// if a module id has a . in it, we should treat that as a /, i.e. files in the module
						// ui.springmvc should go in folder names like .../ui/springmvc/...
						absPath.append(mod.getModuleIdAsPath()).append("/").append(filepath);
						log.debug("Moving file from: {} to {}", name, absPath);
						
						// get the output file
						File outFile = new File(absPath.toString().replace("/", File.separator));
						if (entry.isDirectory()) {
							if (!outFile.exists()) {
								outFile.mkdirs();
							}
						} else {
							// make the parent directories in case it doesn't exist
							File parentDir = outFile.getParentFile();
							if (!parentDir.exists()) {
								parentDir.mkdirs();
							}
							
							// copy the contents over to the webapp for non directories
							outStream = new FileOutputStream(outFile, false);
							inStream = jarFile.getInputStream(entry);
							OpenmrsUtil.copyFile(inStream, outStream);
						}
					} else if ("moduleApplicationContext.xml".equals(name) || "webModuleApplicationContext.xml".equals(name)) {
						moduleNeedsContextRefresh = true;
					} else if (name.equals(mod.getModuleId() + "Context.xml")) {
						String msg = "DEPRECATED: '" + name
						        + "' should be named 'moduleApplicationContext.xml' now. Please update/upgrade. ";
						throw new ModuleException(msg, mod.getModuleId());
					}
				}
			}
			catch (IOException io) {
				log.warn("Unable to copy files from module " + mod.getModuleId() + " to the web layer", io);
			}
			finally {
				if (jarFile != null) {
					try {
						jarFile.close();
					}
					catch (IOException io) {
						log.warn("Couldn't close jar file: " + jarFile.getName(), io);
					}
				}
				if (inStream != null) {
					try {
						inStream.close();
					}
					catch (IOException io) {
						log.warn("Couldn't close InputStream: " + io);
					}
				}
				if (outStream != null) {
					try {
						outStream.close();
					}
					catch (IOException io) {
						log.warn("Couldn't close OutputStream: " + io);
					}
				}
			}
			
			// find and add the dwr code to the dwr-modules.xml file (if defined)
			InputStream inputStream = null;
			try {
				Document config = mod.getConfig();
				Element root = config.getDocumentElement();
				if (root.getElementsByTagName("dwr").getLength() > 0) {
					
					// get the dwr-module.xml file that we're appending our code to
					File f = new File(realPath + "/WEB-INF/dwr-modules.xml".replace("/", File.separator));
					
					// testing if file exists
					if (!f.exists()) {
						// if it does not -> needs to be created
						createDwrModulesXml(realPath);
					}
					
					inputStream = new FileInputStream(f);
					Document dwrmodulexml = getDWRModuleXML(inputStream, realPath);
					Element outputRoot = dwrmodulexml.getDocumentElement();
					
					// loop over all of the children of the "dwr" tag
					Node node = root.getElementsByTagName("dwr").item(0);
					Node current = node.getFirstChild();
					
					while (current != null) {
						if ("allow".equals(current.getNodeName()) || "signatures".equals(current.getNodeName())
						        || "init".equals(current.getNodeName())) {
							((Element) current).setAttribute("moduleId", mod.getModuleId());
							outputRoot.appendChild(dwrmodulexml.importNode(current, true));
						}
						
						current = current.getNextSibling();
					}
					
					moduleNeedsContextRefresh = true;
					
					// save the dwr-modules.xml file.
					OpenmrsUtil.saveDocument(dwrmodulexml, f);
				}
			}
			catch (FileNotFoundException e) {
				throw new ModuleException(realPath + "/WEB-INF/dwr-modules.xml file doesn't exist.", e);
			}
			finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					}
					catch (IOException io) {
						log.error("Error while closing input stream", io);
					}
				}
			}
			
			// mark to delete the entire module web directory on exit
			// this will usually only be used when an improper shutdown has occurred.
			String folderPath = realPath + "/WEB-INF/view/module/" + mod.getModuleIdAsPath();
			File outFile = new File(folderPath.replace("/", File.separator));
			outFile.deleteOnExit();
			
			// additional checks on module needing a context refresh
			if (!moduleNeedsContextRefresh && mod.getAdvicePoints() != null && !mod.getAdvicePoints().isEmpty()) {
				
				// AOP advice points are only loaded during the context refresh now.
				// if the context hasn't been marked to be refreshed yet, mark it
				// now if this module defines some advice
				moduleNeedsContextRefresh = true;
				
			}
			
			// refresh the spring web context to get the just-created xml
			// files into it (if we copied an xml file)
			if (moduleNeedsContextRefresh && !delayContextRefresh) {
				log.debug("Refreshing context for module {}", mod);
				
				try {
					refreshWAC(servletContext, false, mod);
					log.debug("Done Refreshing WAC");
				}
				catch (Exception e) {
					String msg = "Unable to refresh the WebApplicationContext";
					mod.setStartupErrorMessage(msg, e);
					
					if (log.isWarnEnabled()) {
						log.warn(msg + " for module: " + mod.getModuleId(), e);
					}
					
					try {
						stopModule(mod, servletContext, true);
						ModuleFactory.stopModule(mod, true, true); //remove jar from classloader play
					}
					catch (Exception e2) {
						// exception expected with most modules here
						if (log.isWarnEnabled()) {
							log.warn("Error while stopping a module that had an error on refreshWAC", e2);
						}
					}
					
					// try starting the application context again
					refreshWAC(servletContext, false, mod);
					
					notifySuperUsersAboutModuleFailure(mod);
				}
				
			}
			
			if (!delayContextRefresh && ModuleFactory.isModuleStarted(mod)) {
				// only loading the servlets/filters if spring is refreshed because one
				// might depend on files being available in spring
				// if the caller wanted to delay the refresh then they are responsible for
				// calling these two methods on the module
				
				// find and cache the module's servlets
				//(only if the module started successfully previously)
				log.debug("Loading servlets and filters for module {}", mod);
				servletContext.setAttribute(OpenmrsJspServlet.OPENMRS_TLD_SCAN_NEEDED, true);
				loadServlets(mod, servletContext);
				loadFilters(mod, servletContext);
			}
			
			// return true if the module needs a context refresh and we didn't do it here
			return (moduleNeedsContextRefresh && delayContextRefresh);
			
		}
		
		// we aren't processing this module, so a context refresh is not necessary
		return false;
	}
	
	/** Stops all tasks started by given module
	 * @param mod
	 */
	private static void stopTasks(Module mod) {
		SchedulerService schedulerService;
		try {
			schedulerService = Context.getSchedulerService();
		} catch (NullPointerException | APIException e) {
			// if we got here, the scheduler has already been shut down, so there's no work to do
			return;
		}
		
		String modulePackageName = mod.getPackageName();
		for (TaskDefinition task : schedulerService.getRegisteredTasks()) {
			
			String taskClass = task.getTaskClass();
			if (isModulePackageNameInTaskClass(modulePackageName, taskClass)) {
				try {
					schedulerService.shutdownTask(task);
				}
				catch (SchedulerException e) {
					log.error("Couldn't stop task:" + task + " for module: " + mod);
				}
			}
		}
	}
	
	/**
	 * Checks if module package name is in task class name
	 * @param modulePackageName the package name of module
	 * @param taskClass the class of given task
	 * @return true if task and module are in the same package
	 * <strong>Should</strong> return false for different package names
	 * <strong>Should</strong> return false if module has longer package name
	 * <strong>Should</strong> properly match subpackages
	 * <strong>Should</strong> return false for empty package names
	 */
	public static boolean isModulePackageNameInTaskClass(String modulePackageName, String taskClass) {
		return modulePackageName.length() <= taskClass.length()
		        && taskClass.matches(Pattern.quote(modulePackageName) + "(\\..*)+");
	}
	
	/**
	 * Send an Alert to all super users that the given module did not start successfully.
	 *
	 * @param mod The Module that failed
	 */
	private static void notifySuperUsersAboutModuleFailure(Module mod) {
		try {
			// Add the privileges necessary for notifySuperUsers
			Context.addProxyPrivilege(PrivilegeConstants.MANAGE_ALERTS);
			Context.addProxyPrivilege(PrivilegeConstants.GET_USERS);
			
			// Send an alert to all administrators
			Context.getAlertService().notifySuperUsers("Module.startupError.notification.message", null, mod.getName());
		}
		finally {
			// Remove added privileges
			Context.removeProxyPrivilege(PrivilegeConstants.GET_USERS);
			Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_ALERTS);
		}
	}
	
	/**
	 * This method will find and cache this module's servlets (so that it doesn't have to look them
	 * up every time)
	 *
	 * @param mod
	 * @param servletContext the servlet context
	 */
	public static void loadServlets(Module mod, ServletContext servletContext) {
		Element rootNode = mod.getConfig().getDocumentElement();
		NodeList servletTags = rootNode.getElementsByTagName("servlet");
		
		for (int i = 0; i < servletTags.getLength(); i++) {
			Node node = servletTags.item(i);
			NodeList childNodes = node.getChildNodes();
			String name = "", className = "";

			Map<String, String> initParams = new HashMap<>();
			for (int j = 0; j < childNodes.getLength(); j++) {
				Node childNode = childNodes.item(j);
				if ("servlet-name".equals(childNode.getNodeName())) {
					if (childNode.getTextContent() != null) {
						name = childNode.getTextContent().trim();
					}
				} else if ("servlet-class".equals(childNode.getNodeName()) && childNode.getTextContent() != null) {
					className = childNode.getTextContent().trim();
				} else if ("init-param".equals(childNode.getNodeName())) {
					NodeList initParamChildren = childNode.getChildNodes();
					String paramName = null, paramValue = null;
					for (int k = 0; k < initParamChildren.getLength(); k++) {
						Node initParamChild = initParamChildren.item(k);
						if ("param-name".equals(initParamChild.getNodeName()) && initParamChild.getTextContent() != null) {
							paramName = initParamChild.getTextContent().trim();
						} else if ("param-value".equals(initParamChild.getNodeName()) && initParamChild.getTextContent() != null) {
							paramValue = initParamChild.getTextContent().trim();
						}
					}

					if (paramName != null && paramValue != null) {
						initParams.put(paramName, paramValue);
					}
				}
			}
			if (name.length() == 0 || className.length() == 0) {
				log.warn("both 'servlet-name' and 'servlet-class' are required for the 'servlet' tag. Given '" + name
				        + "' and '" + className + "' for module " + mod.getName());
				continue;
			}
			
			HttpServlet httpServlet;
			try {
				httpServlet = (HttpServlet) ModuleFactory.getModuleClassLoader(mod).loadClass(className).newInstance();
			}
			catch (ClassCastException e) {
				log.warn("Class {} from module {} is not a valid HttpServlet", className, mod, e);
				continue;
			}
			catch (ClassNotFoundException e) {
				log.warn("Class {} not found for servlet {} from module {}", className, name, mod, e);
				continue;
			}
			catch (IllegalAccessException e) {
				log.warn("Class {} cannot be accessed for servlet {} from module {}", className, name, mod, e);
				continue;
			}
			catch (InstantiationException e) {
				log.warn("Class {} cannot be instantiated for servlet {} from module {}", className, name, mod, e);
				continue;
			}
			
			try {
				log.debug("Initializing {} servlet. - {}.", name, httpServlet);
				ServletConfig servletConfig = new ModuleServlet.SimpleServletConfig(name, servletContext, initParams);
				httpServlet.init(servletConfig);
			}
			catch (Exception e) {
				log.warn("Unable to initialize servlet {}", name, e);
				throw new ModuleException("Unable to initialize servlet " + name, mod.getModuleId(), e);
			}
			
			// don't allow modules to overwrite servlets of other modules.
			HttpServlet otherServletUsingSameName = MODULE_SERVLETS.get(name);
			if (otherServletUsingSameName != null) {
				String otherServletName = otherServletUsingSameName.getClass().getName();
				throw new ModuleException("A servlet mapping with name " + name + " is already in use and pointing at: "
				        + otherServletName + " from another installed module and this module is trying"
				        + " to use that same name.  Either the module attempting to be installed (" + mod
				        + ") will not work or the other one will not.  Please consult the developers of these two"
				        + " modules to sort this out.");
			}
			
			log.debug("Caching the {} servlet.", name);
			
			SERVLET_LOCK.lock();
			try {
				MODULE_SERVLETS.put(name, httpServlet);
			} finally {
				SERVLET_LOCK.unlock();
			}
		}
	}
	
	/**
	 * Remove the servlets defined for this module
	 *
	 * @param mod the module that is being stopped that needs its servlets removed
	 */
	public static void unloadServlets(Module mod) {
		Element rootNode = mod.getConfig().getDocumentElement();
		NodeList servletTags = rootNode.getElementsByTagName("servlet");
		
		for (int i = 0; i < servletTags.getLength(); i++) {
			Node node = servletTags.item(i);
			NodeList childNodes = node.getChildNodes();
			String name;
			for (int j = 0; j < childNodes.getLength(); j++) {
				Node childNode = childNodes.item(j);
				if ("servlet-name".equals(childNode.getNodeName()) && childNode.getTextContent() != null) {
					name = childNode.getTextContent().trim();
					
					HttpServlet servlet;
					SERVLET_LOCK.lock();
					try {
						servlet = MODULE_SERVLETS.get(name);
					} finally {
						SERVLET_LOCK.unlock();
					}
					
					if (servlet != null) {
						// shut down the servlet
						servlet.destroy();
					}
					
					SERVLET_LOCK.lock();
					try {
						MODULE_SERVLETS.remove(name);
					} finally {
						SERVLET_LOCK.unlock();
					}
				}
			}
		}
	}
	
	/**
	 * This method will initialize and store this module's filters
	 *
	 * @param module - The Module to load and register Filters
	 * @param servletContext - The servletContext within which this method is called
	 */
	public static void loadFilters(Module module, ServletContext servletContext) {
		
		// Load Filters
		Map<String, Filter> filters = new LinkedHashMap<>();
		
		Map<String, Filter> existingFilters;
		FILTERS_LOCK.lock();
		try {
			existingFilters = new HashMap<>(MODULE_FILTERS_BY_NAME);
		} finally {
			FILTERS_LOCK.unlock();
		}
		
		for (ModuleFilterDefinition def : ModuleFilterDefinition.retrieveFilterDefinitions(module)) {
			String name = def.getFilterName();
			String className = def.getFilterClass();
			
			if (existingFilters.containsKey(name)) {
				throw new ModuleException("A filter with the name " + name + " is already in use and pointing at: "
					+ existingFilters.get(name).getClass().getName()
					+ " from another installed module and this module is trying"
					+ " to use that same name.  Either the module attempting to be installed (" + module
					+ ") will not work or the other one will not.  Please consult the developers of these two"
					+ " modules to sort this out.");
			}
			
			ModuleFilterConfig config = ModuleFilterConfig.getInstance(def, servletContext);
			
			Filter filter;
			try {
				filter = (Filter) ModuleFactory.getModuleClassLoader(module).loadClass(className).newInstance();
			}
			catch (ClassCastException e) {
				log.warn("Class {} from module {} is not a valid Filter", className, module, e);
				continue;
			}
			catch (ClassNotFoundException e) {
				log.warn("Class {} not found for servlet {} from module {}", className, name, module, e);
				continue;
			}
			catch (IllegalAccessException e) {
				log.warn("Class {} cannot be accessed for servlet {} from module {}", className, name, module, e);
				continue;
			}
			catch (InstantiationException e) {
				log.warn("Class {} cannot be instantiated for servlet {} from module {}", className, name, module, e);
				continue;
			}
			
			try {
				log.debug("Initializing {} filter. - {}.", name, filter);
				filter.init(config);
			}
			catch (Exception e) {
				log.warn("Unable to initialize servlet {}", name, e);
				throw new ModuleException("Unable to initialize servlet " + name, module.getModuleId(), e);
			}
			
			filters.put(name, filter);
		}

		FILTERS_LOCK.lock();
		try {
			MODULE_FILTERS.put(module, filters.values());
			MODULE_FILTERS_BY_NAME.putAll(filters);
			log.debug("Module {} successfully loaded {} filters.", module, filters.size());
			
			// Load Filter Mappings
			Deque<ModuleFilterMapping> modMappings = ModuleFilterMapping.retrieveFilterMappings(module);
			
			// IMPORTANT: Filter load order
			// retrieveFilterMappings will return the list of filters in the order they occur in the config.xml file
			// here we add them to the *front* of the filter mappings
			modMappings.descendingIterator().forEachRemaining(MODULE_FILTER_MAPPINGS::addFirst);
			
			log.debug("Module {} successfully loaded {} filter mappings.", module, modMappings.size());
		} finally {
			FILTERS_LOCK.unlock();
		}
	}
	
	/**
	 * This method will destroy and remove all filters that were registered by the passed
	 * {@link Module}
	 *
	 * @param module - The Module for which you want to remove and destroy filters.
	 */
	public static void unloadFilters(Module module) {
		
		// Unload Filter Mappings
		for (Iterator<ModuleFilterMapping> mapIter = MODULE_FILTER_MAPPINGS.iterator(); mapIter.hasNext();) {
			ModuleFilterMapping mapping = mapIter.next();
			if (module.equals(mapping.getModule())) {
				mapIter.remove();
				log.debug("Removed ModuleFilterMapping: " + mapping);
			}
		}
		
		// unload Filters
		Collection<Filter> filters = MODULE_FILTERS.get(module);
		if (filters != null) {
			try {
				for (Filter f : filters) {
					f.destroy();
				}
			}
			catch (Exception e) {
				log.warn("An error occurred while trying to destroy and remove module Filter.", e);
			}
			
			log.debug("Module: " + module.getModuleId() + " successfully unloaded " + filters.size() + " filters.");
			MODULE_FILTERS.remove(module);

			MODULE_FILTERS_BY_NAME.values().removeIf(filters::contains);
		}
	}
	
	/**
	 * This method will return all Filters that have been registered a module
	 *
	 * @return A Collection of {@link Filter}s that have been registered by a module
	 */
	public static Collection<Filter> getFilters() {
		return MODULE_FILTERS_BY_NAME.values();
	}
	
	/**
	 * This method will return all Filter Mappings that have been registered by a module
	 *
	 * @return A Collection of all {@link ModuleFilterMapping}s that have been registered by a
	 *         Module
	 */
	public static Collection<ModuleFilterMapping> getFilterMappings() {
		return new ArrayList<>(MODULE_FILTER_MAPPINGS);
	}
	
	/**
	 * Return List of Filters that have been loaded through Modules that have mappings that pass for
	 * the passed request
	 *
	 * @param request - The request to check for matching {@link Filter}s
	 * @return List of all {@link Filter}s that have filter mappings that match the passed request
	 */
	public static List<Filter> getFiltersForRequest(ServletRequest request) {
		
		List<Filter> filters = new ArrayList<>();
		if (request != null) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String requestPath = httpRequest.getRequestURI();
			
			if (requestPath != null) {
				if (requestPath.startsWith(httpRequest.getContextPath())) {
					requestPath = requestPath.substring(httpRequest.getContextPath().length());
				}
				for (ModuleFilterMapping filterMapping : WebModuleUtil.getFilterMappings()) {
					if (ModuleFilterMapping.filterMappingPasses(filterMapping, requestPath)) {
						Filter passedFilter = MODULE_FILTERS_BY_NAME.get(filterMapping.getFilterName());
						if (passedFilter != null) {
							filters.add(passedFilter);
						} else {
							log.warn("Unable to retrieve filter that has a name of " + filterMapping.getFilterName()
							        + " in filter mapping.");
						}
					}
				}
			}
		}
		return filters;
	}
	
	/**
	 * @param inputStream
	 * @param realPath
	 * @return
	 */
	private static Document getDWRModuleXML(InputStream inputStream, String realPath) {
		Document dwrmodulexml;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			// When asked to resolve external entities (such as a DTD) we return an InputSource
			// with no data at the end, causing the parser to ignore the DTD.
			db.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
			dwrmodulexml = db.parse(inputStream);
		}
		catch (Exception e) {
			throw new ModuleException("Error parsing dwr-modules.xml file", e);
		}
		
		return dwrmodulexml;
	}
	
	/**
	 * Reverses all activities done by startModule(org.openmrs.module.Module) Normal stop/shutdown
	 * is done by ModuleFactory
	 */
	public static void shutdownModules(ServletContext servletContext) {
		
		String realPath = getRealPath(servletContext);
		
		// clear the module messages
		String messagesPath = realPath + "/WEB-INF/";
		File folder = new File(messagesPath.replace("/", File.separator));
		
		File[] files = folder.listFiles();
		if (folder.exists() && files != null) {
			Properties emptyProperties = new Properties();
			for (File f : files) {
				if (f.getName().startsWith("module_messages")) {
					OpenmrsUtil.storeProperties(emptyProperties, f, "");
				}
			}
		}
		
		// call web shutdown for each module
		for (Module mod : ModuleFactory.getLoadedModules()) {
			stopModule(mod, servletContext, true);
		}
		
	}
	
	/**
	 * Reverses all visible activities done by startModule(org.openmrs.module.Module)
	 *
	 * @param mod
	 * @param servletContext
	 */
	public static void stopModule(Module mod, ServletContext servletContext) {
		stopModule(mod, servletContext, false);
	}
	
	/**
	 * Reverses all visible activities done by startModule(org.openmrs.module.Module)
	 *
	 * @param mod
	 * @param servletContext
	 * @param skipRefresh
	 */
	public static void stopModule(Module mod, ServletContext servletContext, boolean skipRefresh) {
		
		String moduleId = mod.getModuleId();
		String modulePackage = mod.getPackageName();
		
		// stop all dependent modules
		for (Module dependentModule : ModuleFactory.getStartedModules()) {
			if (!dependentModule.equals(mod) && dependentModule.getRequiredModules().contains(modulePackage)) {
				stopModule(dependentModule, servletContext, skipRefresh);
			}
		}
		
		String realPath = getRealPath(servletContext);
		
		// delete the web files from the webapp
		String absPath = realPath + "/WEB-INF/view/module/" + moduleId;
		File moduleWebFolder = new File(absPath.replace("/", File.separator));
		if (moduleWebFolder.exists()) {
			try {
				OpenmrsUtil.deleteDirectory(moduleWebFolder);
			}
			catch (IOException io) {
				log.warn("Couldn't delete: " + moduleWebFolder.getAbsolutePath(), io);
			}
		}
		
		// (not) deleting module message properties
		
		// remove the module's servlets
		unloadServlets(mod);
		
		// remove the module's filters and filter mappings
		unloadFilters(mod);
		
		// stop all tasks associated with mod
		stopTasks(mod);
		
		// remove this module's entries in the dwr xml file
		InputStream inputStream = null;
		try {
			Document config = mod.getConfig();
			Element root = config.getDocumentElement();
			// if they defined any xml element
			if (root.getElementsByTagName("dwr").getLength() > 0) {
				
				// get the dwr-module.xml file that we're appending our code to
				File f = new File(realPath + "/WEB-INF/dwr-modules.xml".replace("/", File.separator));
				
				// testing if file exists
				if (!f.exists()) {
					// if it does not -> needs to be created
					createDwrModulesXml(realPath);
				}
				
				inputStream = new FileInputStream(f);
				Document dwrmodulexml = getDWRModuleXML(inputStream, realPath);
				Element outputRoot = dwrmodulexml.getDocumentElement();
				
				// loop over all of the children of the "dwr" tag
				// and remove all "allow" and "signature" tags that have the
				// same moduleId attr as the module being stopped
				NodeList nodeList = outputRoot.getChildNodes();
				int i = 0;
				while (i < nodeList.getLength()) {
					Node current = nodeList.item(i);
					if ("allow".equals(current.getNodeName()) || "signatures".equals(current.getNodeName())) {
						NamedNodeMap attrs = current.getAttributes();
						Node attr = attrs.getNamedItem("moduleId");
						if (attr != null && moduleId.equals(attr.getNodeValue())) {
							outputRoot.removeChild(current);
						} else {
							i++;
						}
					} else {
						i++;
					}
				}
				
				// save the dwr-modules.xml file.
				OpenmrsUtil.saveDocument(dwrmodulexml, f);
			}
		}
		catch (FileNotFoundException e) {
			throw new ModuleException(realPath + "/WEB-INF/dwr-modules.xml file doesn't exist.", e);
		}
		finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException io) {
					log.error("Error while closing input stream", io);
				}
			}
		}
		
		if (!skipRefresh) {	
			refreshWAC(servletContext, false, null);
		}
		
	}
	
	/**
	 * Stops, closes, and refreshes the Spring context for the given <code>servletContext</code>
	 *
	 * @param servletContext
	 * @param isOpenmrsStartup if this refresh is being done at application startup
	 * @param startedModule the module that was just started and waiting on the context refresh
	 * @return The newly refreshed webApplicationContext
	 */
	public static XmlWebApplicationContext refreshWAC(ServletContext servletContext, boolean isOpenmrsStartup,
	        Module startedModule) {
		XmlWebApplicationContext wac = (XmlWebApplicationContext) WebApplicationContextUtils
		        .getWebApplicationContext(servletContext);
		log.debug("Refreshing web application Context of class: {}", wac.getClass().getName());
		
		if (dispatcherServlet != null) {
			dispatcherServlet.stopAndCloseApplicationContext();
		}
		
		if (staticDispatcherServlet != null) {
			staticDispatcherServlet.stopAndCloseApplicationContext();
		}
		
		XmlWebApplicationContext newAppContext = (XmlWebApplicationContext) ModuleUtil.refreshApplicationContext(wac,
		    isOpenmrsStartup, startedModule);
		
		try {
			// must "refresh" the spring dispatcherservlet as well to add in
			//the new handlerMappings
			if (dispatcherServlet != null) {
				dispatcherServlet.reInitFrameworkServlet();
			}
			
			if (staticDispatcherServlet != null) {
				staticDispatcherServlet.refreshApplicationContext();
			}
		}
		catch (ServletException se) {
			log.warn("Caught a servlet exception while refreshing the dispatcher servlet", se);
		}
		
		return newAppContext;
	}
	
	/**
	 * Save the dispatcher servlet for use later (reinitializing things)
	 *
	 * @param ds
	 */
	public static void setDispatcherServlet(DispatcherServlet ds) {
		log.debug("Setting dispatcher servlet: " + ds);
		dispatcherServlet = ds;
	}
	
	/**
	 * Save the static content dispatcher servlet for use later when refreshing spring
	 *
	 * @param ds
	 */
	public static void setStaticDispatcherServlet(StaticDispatcherServlet ds) {
		log.debug("Setting dispatcher servlet for static content: " + ds);
		staticDispatcherServlet = ds;
	}
	
	/**
	 * Finds the servlet defined by the servlet name
	 *
	 * @param servletName the name of the servlet out of the path
	 * @return the current servlet or null if none defined
	 */
	public static HttpServlet getServlet(String servletName) {
		return MODULE_SERVLETS.get(servletName);
	}
	
	/**
	 * Retrieves a path to a folder that stores web files of a module. <br>
	 * (path-to-openmrs/WEB-INF/view/module/moduleid)
	 *
	 * @param moduleId module id (e.g., "basicmodule")
	 * @return a path to a folder that stores web files or null if not in a web environment
	 * <strong>Should</strong> return the correct module folder
	 * <strong>Should</strong> return null if the dispatcher servlet is not yet set
	 * <strong>Should</strong> return the correct module folder if real path has a trailing slash
	 */
	public static String getModuleWebFolder(String moduleId) {
		if (dispatcherServlet == null) {
			throw new ModuleException("Dispatcher servlet must be present in the web environment");
		}
		
		String moduleFolder = "WEB-INF/view/module/";
		String realPath = dispatcherServlet.getServletContext().getRealPath("");
		String moduleWebFolder;
		
		//RealPath may contain '/' on Windows when running tests with the mocked servlet context
		if (realPath.endsWith(File.separator) || realPath.endsWith("/")) {
			moduleWebFolder = realPath + moduleFolder;
		} else {
			moduleWebFolder = realPath + "/" + moduleFolder;
		}
		
		moduleWebFolder += moduleId;
		
		return moduleWebFolder.replace("/", File.separator);
	}
	
	public static void createDwrModulesXml(String realPath) {
		
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("dwr");
			doc.appendChild(rootElement);
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(realPath
			        + "/WEB-INF/dwr-modules.xml".replace("/", File.separator)));
			
			transformer.transform(source, result);
			
		}
		catch (ParserConfigurationException pce) {
			log.error("Failed to parse document", pce);
		}
		catch (TransformerException tfe) {
			log.error("Failed to transorm xml source", tfe);
		}
	}

	public static String getRealPath(ServletContext servletContext) {
		return servletContext.getRealPath("");
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/filter/ModuleFilterDefinition.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.openmrs.module.Module;
import org.openmrs.module.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class captures all of the information needed to create and initialize a Filter included in a
 * Module. This object is initialized from an xml element that has the following syntax. Expected
 * XML Format:
 * 
 * <pre>
 * 	&lt;filter&gt;
 * 		&lt;filter-name&gt;MyFilterName&lt;/filter-name&gt;
 * 		&lt;filter-class&gt;Fully qualified classname of the Filter class&lt;/filter-class&gt;
 * 		&lt;init-param&gt;
 * 			&lt;param-name&gt;filterParameterName1&lt;/param-name&gt;
 * 			&lt;param-value&gt;filterParameterValue1&lt;/param-value&gt;
 * 		&lt;/init-param&gt;
 * 	&lt;/filter&gt;
 * </pre>
 */
public class ModuleFilterDefinition implements Serializable {
	
	public static final long serialVersionUID = 1;
	
	private static final Logger log = LoggerFactory.getLogger(ModuleFilterDefinition.class);
	
	// Properties
	private Module module;
	
	private String filterName;
	
	private String filterClass;
	
	private Map<String, String> initParameters = new HashMap<>();
	
	/**
	 * Default constructor, requires a Module
	 * 
	 * @param module - The Module to use to construct this {@link ModuleFilterDefinition}
	 */
	public ModuleFilterDefinition(Module module) {
		this.module = module;
	}
	
	/**
	 * @return - The {@link Module} that registered this FilterDefinition
	 */
	public Module getModule() {
		return module;
	}
	
	/**
	 * @param module the {@link Module} to set
	 */
	public void setModule(Module module) {
		this.module = module;
	}
	
	/**
	 * @return - the name of the Filter
	 */
	public String getFilterName() {
		return filterName;
	}
	
	/**
	 * @param filterName the name of the filter
	 */
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	
	/**
	 * @return - the class name of the filter
	 */
	public String getFilterClass() {
		return filterClass;
	}
	
	/**
	 * @param filterClass the class name of the filter
	 */
	public void setFilterClass(String filterClass) {
		this.filterClass = filterClass;
	}
	
	/**
	 * @return - A map of parameters to use to initialize the filter
	 */
	public Map<String, String> getInitParameters() {
		return initParameters;
	}
	
	/**
	 * #param - A map of parameters to use to initialize the filter
	 */
	public void setInitParameters(Map<String, String> initParameters) {
		this.initParameters = initParameters;
	}
	
	/**
	 * Adds a Parameter that should be passed in to initialize this Filter
	 * 
	 * @param parameterName - The name of the parameter
	 * @param parameterValue - The value of the parameter
	 */
	public void addInitParameter(String parameterName, String parameterValue) {
		this.initParameters.put(parameterName, parameterValue);
	}
	
	// Static methods
	
	/**
	 * Static method to parse through a Module's configuration file and return a List of
	 * ModuleFilterDefinition objects for which there are configuration elements. Expected XML
	 * Format:
	 * 
	 * <pre>
	 * 	&lt;filter&gt;
	 * 		&lt;filter-name&gt;MyFilterName&lt;/filter-name&gt;
	 * 		&lt;filter-class&gt;Fully qualified classname of the Filter class&lt;/filter-class&gt;
	 * 		&lt;init-param&gt;
	 * 			&lt;param-name&gt;filterParameterName1&lt;/param-name&gt;
	 * 			&lt;param-value&gt;filterParameterValue1&lt;/param-value&gt;
	 * 		&lt;/init-param&gt;
	 * 	&lt;/filter&gt;
	 * </pre>
	 * 
	 * @param module - The {@link Module} for which to retrieve filter the defined
	 *            {@link ModuleFilterDefinition}s
	 * @return List of {@link ModuleFilterDefinition}s that have been defined for the passed
	 *         {@link Module}
	 */
	public static List<ModuleFilterDefinition> retrieveFilterDefinitions(Module module)  {
		List<ModuleFilterDefinition> filters = new ArrayList<>();
		
		try {
			Element rootNode = module.getConfig().getDocumentElement();
			NodeList filterNodes = rootNode.getElementsByTagName("filter");
			if (filterNodes.getLength() > 0) {
				for (int i = 0; i < filterNodes.getLength(); i++) {
					ModuleFilterDefinition filter = new ModuleFilterDefinition(module);
					Node node = filterNodes.item(i);
					NodeList configNodes = node.getChildNodes();
					for (int j = 0; j < configNodes.getLength(); j++) {
						Node configNode = configNodes.item(j);
						switch (configNode.getNodeName()) {
							case "filter-name":
								filter.setFilterName(configNode.getTextContent().trim());
								break;
							case "filter-class":
								filter.setFilterClass(configNode.getTextContent().trim());
								break;
							case "init-param":
								NodeList paramNodes = configNode.getChildNodes();
								String paramName = "";
								String paramValue = "";
								for (int k = 0; k < paramNodes.getLength(); k++) {
									Node paramNode = paramNodes.item(k);
									if ("param-name".equals(paramNode.getNodeName())) {
										paramName = paramNode.getTextContent().trim();
									} else if ("param-value".equals(paramNode.getNodeName())) {
										paramValue = paramNode.getTextContent().trim();
									}
								}
								filter.addInitParameter(paramName, paramValue);
								break;
						}
					}
					filters.add(filter);
				}
			}
		}
		catch (Exception e) {
			throw new ModuleException("Unable to parse filters in module configuration.", e);
		}
		
		log.debug("Retrieved {} filters for {}: {}", filters.size(), module, filters);
		return filters;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/filter/ModuleFilter.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.openmrs.module.web.WebModuleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter provides a mechanism for modules to plug-in their own custom filters. It is started
 * automatically, and will iterate through all filters that have been added through Modules.
 */
public class ModuleFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(ModuleFilter.class);
	
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Initializating ModuleFilter");
	}
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		log.debug("Destroying the ModuleFilter");
	}
	
	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	        ServletException {
		ModuleFilterChain moduleChain = ModuleFilterChain.getInstance(WebModuleUtil.getFiltersForRequest(request), chain);
		moduleChain.doFilter(request, response);
	}
	
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/filter/ModuleFilterConfig.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web.filter;

import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * This class is an implementation of FilterConfig for use in instantiating Filters from Modules
 */
public class ModuleFilterConfig implements FilterConfig {
	
	// Properties
	private ModuleFilterDefinition filterDefinition;
	
	private ServletContext servletContext;
	
	/**
	 * Private constructor which sets all required properties
	 * 
	 * @param filterDefinition The ModuleFilterDefinition to store in this ModuleFilterConfig
	 * @param servletContext The {@link ServletContext} to store in this ModuleFilterConfig
	 */
	private ModuleFilterConfig(ModuleFilterDefinition filterDefinition, ServletContext servletContext) {
		this.filterDefinition = filterDefinition;
		this.servletContext = servletContext;
	}
	
	/**
	 * Factory method to construct and return a ModuleFilterConfig
	 * 
	 * @param filterDefinition The ModuleFilterDefinition to store in this ModuleFilterConfig
	 * @param servletContext The {@link ServletContext} to store in this ModuleFilterConfig
	 * @return The ModuleFilterConfig that is fully initialized with the passed parameters
	 */
	public static ModuleFilterConfig getInstance(ModuleFilterDefinition filterDefinition, ServletContext servletContext) {
		return new ModuleFilterConfig(filterDefinition, servletContext);
	}
	
	/**
	 * @see javax.servlet.FilterConfig#getFilterName()
	 */
	@Override
	public String getFilterName() {
		return filterDefinition.getFilterName();
	}
	
	/**
	 * @see javax.servlet.FilterConfig#getInitParameter(java.lang.String)
	 */
	@Override
	public String getInitParameter(String paramName) {
		return filterDefinition.getInitParameters().get(paramName);
	}
	
	/**
	 * @see javax.servlet.FilterConfig#getInitParameterNames()
	 */
	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(filterDefinition.getInitParameters().keySet());
	}
	
	//******************
	// Property access
	//******************
	
	public ModuleFilterDefinition getFilterDefinition() {
		return filterDefinition;
	}
	
	public void setFilterDefinition(ModuleFilterDefinition filterDefinition) {
		this.filterDefinition = filterDefinition;
	}
	
	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/filter/ModuleFilterMapping.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web.filter;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.openmrs.module.Module;
import org.openmrs.module.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class represents the mapping of a Filter to a collection of Servlets and URLs
 */
public class ModuleFilterMapping implements Serializable {
	
	public static final long serialVersionUID = 1;
	
	private static final Logger log = LoggerFactory.getLogger(ModuleFilterMapping.class);
	
	private static final Deque<ModuleFilterMapping> EMPTY_DEQUE = new ArrayDeque<>(0);
	
	// Properties
	private Module module;
	
	private String filterName;
	
	private List<String> servletNames = new ArrayList<>();
	
	private List<String> urlPatterns = new ArrayList<>();
	
	/**
	 * Default constructor, requires a Module
	 * 
	 * @param module - the module to use to construct this ModuleFilterMapping
	 */
	public ModuleFilterMapping(Module module) {
		this.module = module;
	}
	
	/**
	 * @return - the {@link Module} that registered this FilterDefinition
	 */
	public Module getModule() {
		return module;
	}
	
	/**
	 * @param module the {@link Module}
	 */
	public void setModule(Module module) {
		this.module = module;
	}
	
	/**
	 * @return the name of the Filter
	 */
	public String getFilterName() {
		return filterName;
	}
	
	/**
	 * @param filterName the name of the Filter
	 */
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	
	/**
	 * @return a List of all Servlet Names mapped to this Filter
	 */
	public List<String> getServletNames() {
		return servletNames;
	}
	
	/**
	 * @param servletNames a List of all Servlet Names mapped to this filter
	 */
	public void setServletNames(List<String> servletNames) {
		this.servletNames = servletNames;
	}
	
	/**
	 * Adds a Servlet name to the List of those mapped to this filter
	 * 
	 * @param servletName - The servlet name to add
	 */
	public void addServletName(String servletName) {
		this.servletNames.add(servletName);
	}
	
	/**
	 * @return - a List of all Url Patterns mapped to this filter
	 */
	public List<String> getUrlPatterns() {
		return urlPatterns;
	}
	
	/**
	 * @param urlPatterns a List of all Url Patterns mapped to this filter
	 */
	public void setUrlPatterns(List<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}
	
	/**
	 * Adds a Url pattern to the List of those mapped to this filter
	 * 
	 * @param urlPattern - The urlPattern to add
	 */
	public void addUrlPattern(String urlPattern) {
		this.urlPatterns.add(urlPattern);
	}
	
	/**
	 * Return <code>true</code> if the passed Filter passes one or more filter mappings otherwise,
	 * return <code>false</code>.
	 * 
	 * @param filterMapping - The {@link ModuleFilterMapping} to check for matching servlets and url
	 *            patterns
	 * @param requestPath - The URI of the request to check against the {@link ModuleFilterMapping},
	 * 	      with the context path already removed (since module filter mappings are relative to the
	 *        context path).
	 * @return - true if the given {@link ModuleFilterMapping} matches the passed requestPath For
	 *         example: Passing a ModuleFilterMapping containing a urlPattern of "*" would return
	 *         true for any requestPath Passing a ModuleFilterMapping containing a urlPattern of
	 *         "*.jsp" would return true for any requestPath ending in ".jsp"
	 * <strong>Should</strong> return false if the requestPath is null
	 * <strong>Should</strong> return true if the ModuleFilterMapping contains any matching urlPatterns for this
	 *         requestPath
	 * <strong>Should</strong> return true if the ModuleFilterMapping contains any matching servletNames for this
	 *         requestPath
	 * <strong>Should</strong> return false if no matches are found for this requestPath
	 */
	public static boolean filterMappingPasses(ModuleFilterMapping filterMapping, String requestPath) {
		
		// Return false if url is null
		if (requestPath == null) {
			return false;
		}
		
		for (String patternToCheck : filterMapping.getUrlPatterns()) {
			if (urlPatternMatches(patternToCheck, requestPath)) {
				return true;
			}
		}
		for (String patternToCheck : filterMapping.getServletNames()) {
			if (servletNameMatches(patternToCheck, requestPath)) {
				return true;
			}
		}
		
		// If none found, return false
		return false;
	}
	
	/**
	 * Return <code>true</code> if the context-relative request path matches the patternToCheck
	 * otherwise, return <code>false</code>.
	 * 
	 * @param patternToCheck String pattern to check
	 * @param requestPath to check
	 * <strong>Should</strong> return false if the patternToCheck is null
	 * <strong>Should</strong> return true if the pattern is *
	 * <strong>Should</strong> return true if the pattern is /*
	 * <strong>Should</strong> return true if the pattern matches the requestPath exactly
	 * <strong>Should</strong> return true if the pattern matches everything up to a suffix of /*
	 * <strong>Should</strong> return true if the pattern matches by extension
	 * <strong>Should</strong> return false if no pattern matches
	 */
	public static boolean urlPatternMatches(String patternToCheck, String requestPath) {
		
		// Return false if patternToCheck is null
		if (patternToCheck == null) {
			return false;
		}
		
		log.debug("Checking URL <" + requestPath + "> against pattern <" + patternToCheck + ">");
		
		// Match exact or full wildcard
		if ("*".equals(patternToCheck) || "/*".equals(patternToCheck) || patternToCheck.equals(requestPath)) {
			return true;
		}
		
		// Match wildcard
		if (patternToCheck.endsWith("/*")) {
			int patternLength = patternToCheck.length() - 2;
			if (patternToCheck.regionMatches(0, requestPath, 0, patternLength)) {
				return requestPath.length() == patternLength || '/' == requestPath.charAt(patternLength);
			}
			return false;
		}
		
		// Case 3 - Extension Match
		if (patternToCheck.startsWith("*.")) {
			int slash = requestPath.lastIndexOf('/');
			int period = requestPath.lastIndexOf('.');
			int reqLen = requestPath.length();
			int patLen = patternToCheck.length();
			
			if (slash >= 0 && period > slash && period != reqLen - 1 && reqLen - period == patLen - 1) {
				return (patternToCheck.regionMatches(2, requestPath, period + 1, patLen - 2));
			}
		}
		
		// If no match found by here, return false
		return false;
	}
	
	/**
	 * Return <code>true</code> if the specified servlet name matches the filterMapping otherwise
	 * return <code>false</code>.
	 * 
	 * @param patternToCheck String pattern to check
	 * @param servletName Servlet Name to check
	 * <strong>Should</strong> return false if the patternToCheck is null
	 * <strong>Should</strong> return true if the pattern is *
	 * <strong>Should</strong> return true if the pattern matches the servlet name exactly
	 * <strong>Should</strong> return false if no pattern matches
	 */
	public static boolean servletNameMatches(String patternToCheck, String servletName) {
		
		// Return false if servletName is null
		if (servletName == null) {
			return false;
		}
		
		log.debug("Checking servlet <" + servletName + "> against pattern <" + patternToCheck + ">");
		
		// Match exact or full wildcard
		return ("*").equals(patternToCheck) || servletName.equals(patternToCheck);
		
		// If none found, return false
	}
	
	/**
	 * Static method to parse through a Module's configuration file and return a List of
	 * ModuleFilterMapping objects for which there are configuration elements. Expected XML Format:
	 * 
	 * <pre>
	 * 	&lt;filter-mapping&gt;
	 * 		&lt;filter-name&gt;MyFilterName&lt;/filter-name&gt;
	 * 		&lt;url-pattern&gt;The pattern of URLs to match&lt;/filter-class&gt;
	 * 	&lt;/filter-mapping&gt;
	 * or
	 * 	&lt;filter-mapping&gt;
	 * 		&lt;filter-name&gt;MyFilterName&lt;/filter-name&gt;
	 * 		&lt;servlet-name&gt;The servlet name to match&lt;/servlet-name&gt;
	 * 	&lt;/filter-mapping&gt;
	 * </pre>
	 * 
	 * @param module - The {@link Module} for which you want to retrieve the defined
	 *            {@link ModuleFilterMapping}s
	 * @return - a {@link Deque} of {@link ModuleFilterMapping}s that are defined for the passed
	 *         {@link Module}
	 */
	public static Deque<ModuleFilterMapping> retrieveFilterMappings(Module module){
		Deque<ModuleFilterMapping> mappings;
		
		try {
			Element rootNode = module.getConfig().getDocumentElement();
			NodeList mappingNodes = rootNode.getElementsByTagName("filter-mapping");
			if (mappingNodes.getLength() > 0) {
				mappings = new ArrayDeque<>(mappingNodes.getLength());
				for (int i = 0; i < mappingNodes.getLength(); i++) {
					ModuleFilterMapping mapping = new ModuleFilterMapping(module);
					Node node = mappingNodes.item(i);
					NodeList configNodes = node.getChildNodes();
					for (int j = 0; j < configNodes.getLength(); j++) {
						Node configNode = configNodes.item(j);
						switch (configNode.getNodeName()) {
							case "filter-name":
								mapping.setFilterName(configNode.getTextContent());
								break;
							case "url-pattern":
								mapping.addUrlPattern(configNode.getTextContent());
								break;
							case "servlet-name":
								mapping.addServletName(configNode.getTextContent());
								break;
						}
					}
					mappings.add(mapping);
				}
				
				log.debug("Retrieved {} filter-mappings for {}: {}", mappings.size(), module, mappings);
				return mappings;
			}
		}
		catch (Exception e) {
			throw new ModuleException("Unable to parse filters in module configuration.", e);
		}
		
		return EMPTY_DEQUE;
	}
}

File path: openmrs-core/web/src/main/java/org/openmrs/module/web/filter/ModuleFilterChain.java
Code is: 
/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.web.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This class is an implementation of FilterChain for use in using Filters defined within Modules.
 * It enables the Module system to iterate through all of the defined Filters before continuing down
 * the initial filter chain.
 */
public class ModuleFilterChain implements FilterChain {
	
	// Properties
	private Iterator<Filter> filterIterator;
	
	private FilterChain initialFilterChain;
	
	/**
	 * Private constructor which sets all required properties
	 * 
	 * @param filters: The Collection of {@link Filter}s that this FilterChain will iterate over
	 *            before returning control back the the <code>initialFilterChain</code>
	 * @param initialFilterChain: The {@link FilterChain} to return control to once all of the
	 *            {@link Filter}s have been executed
	 */
	private ModuleFilterChain(Collection<Filter> filters, FilterChain initialFilterChain) {
		this.filterIterator = filters.iterator();
		this.initialFilterChain = initialFilterChain;
	}
	
	/**
	 * Factory method to construct and return a ModuleFilterChain
	 * 
	 * @param filters The Collection of {@link Filter}s that this FilterChain will iterate over
	 *            before returning control back to the <code>initialFilterChain</code>
	 * @param initialFilterChain The {@link FilterChain} to return control to once all of the
	 *            {@link Filter}s have been executed
	 * @return The ModuleFilterChain that is fully initialized with the passed parameters
	 */
	public static ModuleFilterChain getInstance(Collection<Filter> filters, FilterChain initialFilterChain) {
		return new ModuleFilterChain(filters, initialFilterChain);
	}
	
	/**
	 * This Iterates across all of the Filters defined by modules before handing control back over
	 * to the initial filter chain to continue on.
	 * 
	 * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (filterIterator.hasNext()) {
			Filter f = filterIterator.next();
			f.doFilter(request, response, this);
		} else {
			initialFilterChain.doFilter(request, response);
		}
	}
}

