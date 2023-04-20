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
import { Test, TestingModule } from '@nestjs/testing';
import { NodeDescriptionService } from './node.description.service';
import { NodeDescRepository } from '../repositories/node.desc.repository';

describe('Test NodeDescriptionService', () => {
  let module: TestingModule;
  let repository: NodeDescRepository;
  let service: NodeDescriptionService;

  beforeAll(async () => {
    module = await Test.createTestingModule({
      providers: [
        {
          provide: NodeDescRepository,
          useValue: {
            selectDescriptionByNodeId: jest.fn(),
          },
        },
        NodeDescriptionService,
      ],
    }).compile();
    service = module.get<NodeDescriptionService>(NodeDescriptionService);
    repository = module.get<NodeDescRepository>(NodeDescRepository);
  });

  beforeEach(() => {
    jest.spyOn(repository, 'selectDescriptionByNodeId').mockResolvedValue({ description: 'node description' });
  });

  it('should be return node description', async () => {
    const nodeDescription = await service.getDescription('nodeId');
    expect(nodeDescription).toEqual('node description');
  });
});
