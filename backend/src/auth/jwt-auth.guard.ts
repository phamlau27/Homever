import { Injectable, CanActivate, ExecutionContext, UnauthorizedException } from '@nestjs/common';
import { CognitoService } from './cognito.service';

@Injectable()
export class JwtAuthGuard implements CanActivate {
  constructor(private cognitoService: CognitoService) {}

  async canActivate(context: ExecutionContext): Promise<boolean> {
    const request = context.switchToHttp().getRequest();
    const authHeader = request.headers.authorization;
    if (!authHeader) {
      throw new UnauthorizedException('Missing token');
    }

    const token = authHeader.split(' ')[1];
    if (!token) {
      throw new UnauthorizedException('Invalid token format');
    }

    try {
      const payload = await this.cognitoService.verifyToken(token);
      request.user = payload;
      return true;
    } catch (e) {
      throw new UnauthorizedException('Invalid or expired token');
    }
  }
}
