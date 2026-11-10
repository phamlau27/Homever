import { Module, Global } from '@nestjs/common';
import { CognitoService } from './cognito.service';
import { JwtAuthGuard } from './jwt-auth.guard';

@Global()
@Module({
  providers: [CognitoService, JwtAuthGuard],
  exports: [CognitoService, JwtAuthGuard],
})
export class AuthModule {}
