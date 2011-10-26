/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * These authors would like to acknowledge the Spanish Ministry of Industry,
 * Tourism and Trade, for the support in the project TSI020301-2008-2
 * "PIRAmIDE: Personalizable Interactions with Resources on AmI-enabled
 * Mobile Dynamic Environments", led by Treelogic
 * ( http://www.treelogic.com/ ):
 *
 *   http://www.piramidepse.com/
 */
package com.phonegap.plugins.barcodescanner.google.zxing.oned.rss.expanded.decoders;

import com.phonegap.plugins.barcodescanner.google.zxing.NotFoundException;
import com.phonegap.plugins.barcodescanner.google.zxing.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
final class AI01393xDecoder extends AI01decoder {

  private static final int headerSize = 5 + 1 + 2;
  private static final int lastDigitSize = 2;
  private static final int firstThreeDigitsSize = 10;

  AI01393xDecoder(BitArray information) {
    super(information);
  }

  public String parseInformation() throws NotFoundException {
    if(this.information.size < headerSize + gtinSize) {
      throw NotFoundException.getNotFoundInstance();
    }

    StringBuffer buf = new StringBuffer();

    encodeCompressedGtin(buf, headerSize);

    int lastAIdigit =
        this.generalDecoder.extractNumericValueFromBitArray(headerSize + gtinSize, lastDigitSize);

    buf.append("(393");
    buf.append(lastAIdigit);
    buf.append(')');

    int firstThreeDigits =
        this.generalDecoder.extractNumericValueFromBitArray(headerSize + gtinSize + lastDigitSize, firstThreeDigitsSize);
    if(firstThreeDigits / 100 == 0) {
      buf.append('0');
    }
    if(firstThreeDigits / 10 == 0) {
      buf.append('0');
    }
    buf.append(firstThreeDigits);

    DecodedInformation generalInformation =
        this.generalDecoder.decodeGeneralPurposeField(headerSize + gtinSize + lastDigitSize + firstThreeDigitsSize, null);
    buf.append(generalInformation.getNewString());

    return buf.toString();
  }
}
