/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

/**
 * @author tejas.kanani
 */
public abstract class LandingPageType {

	public abstract String getLandingPagePath(HttpServletRequest request)
			throws PortalException, SystemException;
}
