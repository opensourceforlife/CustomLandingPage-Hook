/**
 * 
 */

package com.liferay.opensourceforlife.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.opensourceforlife.landingpage.LandingPageType;
import com.liferay.opensourceforlife.landingpage.LandingPageTypeFactory;
import com.liferay.opensourceforlife.util.CustomLandingPageConstant;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Tejas Kanani
 */
public class CustomLandingPageAction extends Action
{

	@Override
	public void run(final HttpServletRequest request, final HttpServletResponse response)
			throws ActionException
	{
		try
		{
			doRun(request, response);
		} catch (Exception e)
		{
			throw new ActionException(e);
		}
	}

	protected void doRun(final HttpServletRequest request, final HttpServletResponse response)
			throws SystemException, PortalException
	{
		long companyId = PortalUtil.getCompanyId(request);

		String path = PrefsPropsUtil.getString(companyId, PropsKeys.DEFAULT_LANDING_PAGE_PATH);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(PropsKeys.DEFAULT_LANDING_PAGE_PATH + StringPool.EQUAL + path);
		}

		// Check for override.default.landing.page.path property value
		boolean overrideDefaultLandingPagePath = PrefsPropsUtil.getBoolean(companyId,
				CustomLandingPageConstant.OVERRIDE_DEFAULT_LANDING_PAGE_PATH);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(CustomLandingPageConstant.OVERRIDE_DEFAULT_LANDING_PAGE_PATH
					+ StringPool.EQUAL + overrideDefaultLandingPagePath);
		}

		if ((Validator.isNull(overrideDefaultLandingPagePath) || !overrideDefaultLandingPagePath)
				&& LOG.isInfoEnabled())
		{
			LOG.info("Please set 'override.default.landing.page.path=true' "
					+ "in hook's portal.properties to enable the functionality "
					+ "provided by Custom Landing Page Hook");
		}

		if (overrideDefaultLandingPagePath)
		{
			String customLandingPath = getCustomLandingPage(request);
			if (Validator.isNull(customLandingPath))
			{
				// set portal context if path is blank, which is needed if you are using non root
				// context for your liferay instance
				String portalContext = CustomLandingPageConstant.PORTAL_CONTEXT;
				if (Validator.isNotNull(portalContext))
				{
					customLandingPath = portalContext;
				}
			}
			path = customLandingPath;
		}

		if (LOG.isInfoEnabled())
		{
			LOG.info("Custom Landing Page path" + StringPool.EQUAL + path);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Custom Landing Page path" + StringPool.EQUAL + path + " for User : "
					+ PortalUtil.getUser(request).getFullName());
		}

		HttpSession session = request.getSession();
		session.setAttribute(WebKeys.LAST_PATH, new LastPath(StringPool.BLANK, path));
	}

	/**
	 * Returns custom landing page path after user login
	 * 
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private String getCustomLandingPage(final HttpServletRequest request) throws PortalException,
			SystemException
	{
		long companyId = PortalUtil.getCompanyId(request);
		String customLandingPagePath = StringPool.BLANK;

		String landingPageTypeSelection = PrefsPropsUtil.getString(companyId,
				CustomLandingPageConstant.CUSTOM_LANDING_PAGE_TYPE,
				CustomLandingPageConstant.DEFAULT_LANDING_PAGE_TYPE);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(CustomLandingPageConstant.CUSTOM_LANDING_PAGE_TYPE + StringPool.EQUAL
					+ landingPageTypeSelection);
		}

		if (Validator.isNotNull(landingPageTypeSelection))
		{
			LandingPageType landingPageType = LandingPageTypeFactory
					.getLandingPageTypeInstance(landingPageTypeSelection);
			customLandingPagePath = landingPageType.getLandingPagePath(request);
		}

		return customLandingPagePath;
	}

	private static final Log LOG = LogFactory.getLog(CustomLandingPageAction.class);
}
