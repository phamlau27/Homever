import { Module, Global } from '@nestjs/common';
import { StorageService } from './storage.service';
import { StorageController } from './storage.controller';
import { S3Service } from './s3.service';

@Global()
@Module({
  controllers: [StorageController],
  providers: [StorageService, S3Service],
  exports: [StorageService, S3Service],
})
export class StorageModule {}
