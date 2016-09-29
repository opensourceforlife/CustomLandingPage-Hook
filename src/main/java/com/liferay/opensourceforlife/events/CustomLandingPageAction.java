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
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
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
		} catch (final Exception e)
		{
			throw new ActionException(e);
		}
	}

	protected void doRun(final HttpServletRequest request, final HttpServletResponse response)
			throws SystemException, PortalException
	{
		final long companyId = PortalUtil.getCompanyId(request);

		String path = PrefsPropsUtil.getString(companyId, PropsKeys.DEFAULT_LANDING_PAGE_PATH);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(PropsKeys.DEFAULT_LANDING_PAGE_PATH + StringPool.EQUAL + path);
		}

		// Check for override.default.landing.page.path property value
		final boolean overrideDefaultLandingPagePath = PrefsPropsUtil.getBoolean(companyId,
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
					+ "in hook's portal.properties to enable user to land on custom landing page using "
					+ "Custom Landing Page Hook");
		}

		if (overrideDefaultLandingPagePath)
		{
			path = getCustomLandingPage(request);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Custom Landing Page path" + StringPool.EQUAL + path + " for User : "
						+ PortalUtil.getUser(request).getFullName());
			}
		}
		if (Validator.isNotNull(path))
		{
			if (path.contains("${liferay:screenName}") || path.contains("${liferay:userId}"))
			{
				final User user = PortalUtil.getUser(request);
				if (Validator.isNotNull(user))
				{
					path = StringUtil.replace(path, new String[]
					{ "${liferay:screenName}", "${liferay:userId}" }, new String[]
					{ HtmlUtil.escapeURL(user.getScreenName()), String.valueOf(user.getUserId()) });
				}
			}
		}

		if (Validator.isNotNull(path))
		{
			final HttpSession session = request.getSession();
			session.setAttribute(WebKeys.LAST_PATH, new LastPath(StringPool.BLANK, path));
		}
	}

	/**
	 * Returns custom landing page path after user login
	 *
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	private String getCustomLandingPage(final HttpServletRequest request)
			throws PortalException, SystemException
	{
		final long companyId = PortalUtil.getCompanyId(request);
		String customLandingPagePath = StringPool.BLANK;

		final String landingPageTypeSelection = PrefsPropsUtil.getString(companyId,
				CustomLandingPageConstant.CUSTOM_LANDING_PAGE_TYPE,
				CustomLandingPageConstant.DEFAULT_LANDING_PAGE_TYPE);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(CustomLandingPageConstant.CUSTOM_LANDING_PAGE_TYPE + StringPool.EQUAL
					+ landingPageTypeSelection);
		}

		if (Validator.isNotNull(landingPageTypeSelection))
		{
			final LandingPageType landingPageType = LandingPageTypeFactory
					.getLandingPageTypeInstance(landingPageTypeSelection);
			customLandingPagePath = landingPageType.getLandingPagePath(request);
		}

		return customLandingPagePath;
	}

	private static final Log LOG = LogFactory.getLog(CustomLandingPageAction.class);
}