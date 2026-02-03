package org.silverpeas.mobile.server.helpers;

import org.silverpeas.kernel.bundle.ResourceLocator;
import org.silverpeas.kernel.bundle.SettingBundle;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import java.io.InputStream;

public class AntivirusHelper {

    public static AntivirusResult scan(InputStream file) {
        AntivirusResult analyseResult = new AntivirusResult();
        analyseResult.setError(false);
        analyseResult.setSafe(true);
        if (getSettings().getBoolean("antivirus.enable", false)) {

            ClamavClient client = new ClamavClient(getSettings().getString("antivirus.host", "localhost"), getSettings().getInteger("antivirus.port", 3310));
            ScanResult result = client.scan(file);
            String scanOutput = result.toString();
            analyseResult.setSafe(false);
            if (scanOutput.contains("FOUND")) {
                String virusName = scanOutput.substring(scanOutput.indexOf(": ") + 2, scanOutput.indexOf(" FOUND"));
                analyseResult.setVirusName(virusName);
            } else if (scanOutput.contains("OK")) {
                analyseResult.setSafe(true);
            } else {
                analyseResult.setError(true);
                analyseResult.setErrorMessage(scanOutput);
            }
        }
        return analyseResult;
    }

    protected static SettingBundle getSettings() {
        return ResourceLocator.getSettingBundle("org.silverpeas.mobile.mobileSettings");
    }

}
