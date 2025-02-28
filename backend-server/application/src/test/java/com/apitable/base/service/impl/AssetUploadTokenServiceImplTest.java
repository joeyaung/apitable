/*
 * APITable <https://github.com/apitable/apitable>
 * Copyright (C) 2022 APITable Ltd. <https://apitable.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.apitable.base.service.impl;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.apitable.AbstractIntegrationTest;
import com.apitable.asset.enums.AssetType;
import com.apitable.asset.service.IAssetUploadTokenService;
import com.apitable.core.exception.BusinessException;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;

public class AssetUploadTokenServiceImplTest extends AbstractIntegrationTest {

    @Resource
    private IAssetUploadTokenService iAssetUploadTokenService;

    @Test
    public void testCreateSpaceAssetPreSignedUrlParameterException() {
        assertThatCode(() -> iAssetUploadTokenService.createSpaceAssetPreSignedUrl(null, null, AssetType.DATASHEET.getValue(), 1)).isInstanceOf(BusinessException.class);

        assertThatCode(() -> iAssetUploadTokenService.createSpaceAssetPreSignedUrl(null, "", AssetType.DATASHEET.getValue(), 101)).isInstanceOf(BusinessException.class);
    }
}