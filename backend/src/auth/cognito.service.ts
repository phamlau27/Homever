import { Injectable, UnauthorizedException } from '@nestjs/common';
import { CognitoJwtVerifier } from 'aws-jwt-verify';

@Injectable()
export class CognitoService {
  private accessVerifier: any;
  private idVerifier: any;

  constructor() {
    const userPoolId = process.env.COGNITO_USER_POOL_ID || 'ap-southeast-1_xCmijwwpz';
    const clientId = process.env.COGNITO_CLIENT_ID || '6r9ajmeb3b63e9j4qhvvfcm82n';

    this.accessVerifier = CognitoJwtVerifier.create({
      userPoolId,
      tokenUse: 'access',
      clientId,
    });

    this.idVerifier = CognitoJwtVerifier.create({
      userPoolId,
      tokenUse: 'id',
      clientId,
    });
  }

  async verifyToken(token: string) {
    try {
      return await this.accessVerifier.verify(token);
    } catch (accessError: any) {
      try {
        return await this.idVerifier.verify(token);
      } catch (idError: any) {
        if (accessError?.name === 'JwtExpiredError') {
          console.warn(`[CognitoService] Token has expired: ${accessError.message}`);
          throw new UnauthorizedException('Token has expired');
        }
        console.error('[CognitoService] Verify error:', accessError?.message || accessError);
        throw new UnauthorizedException('Token is invalid');
      }
    }
  }
}
