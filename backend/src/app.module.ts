import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { DatabaseModule } from './database/database.module';
import { AuthModule } from './auth/auth.module';
import { StorageModule } from './storage/storage.module';
import { UsersModule } from './users/users.module';

@Module({
  imports: [DatabaseModule, AuthModule, StorageModule, UsersModule],
  controllers: [AppController],
  providers: [],
})
export class AppModule {}
