/**
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

import { Typography, useThemeColors } from '@apitable/components';
import { integrateCdnHost } from '@apitable/core';
import { getEnvVariables } from 'pc/utils/env';
import { NavBar } from './components/nav_bar';
import styles from './style.module.less';

export const HomeWrapper: React.FC<React.PropsWithChildren<unknown>> = ({ children }) => {
 
  const colors = useThemeColors();

  return (
    <div className={styles.pcHome}>
      <div className={styles.header}>
        <div className={styles.brand}>
          <img src={integrateCdnHost(getEnvVariables().LOGIN_LOGO!)} width={132} alt="logo" />
          <Typography variant={'h7'} color={colors.textCommonSecondary}>
            {"let's make the world more productive!"}
          </Typography>
        </div>
      </div>
      <div className={styles.main}>
        { children }
      </div>
      <div className={styles.footer}>
        <NavBar />
      </div>
    </div>
  );
};