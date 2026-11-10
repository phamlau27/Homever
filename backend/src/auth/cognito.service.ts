import { Injectable, UnauthorizedException } from '@nestjs/common';
import { CognitoJwtVerifier } from 'aws-jwt-verify';

@Injectable()
export class CognitoService {
  private verifier: any;

  constructor() {
    this.verifier = CognitoJwtVerifier.create({
      userPoolId: process.env.COGNITO_USER_POOL_ID || 'ap-southeast-1_xCmijwwpz',
      tokenUse: 'access',
      clientId: process.env.COGNITO_CLIENT_ID || '6r9ajmeb3b63e9j4qhvvfcm82n',
    });
  }

  async verifyToken(token: string) {
    try {
      const payload = await this.verifier.verify(token);
      return payload;
    } catch (error) {
      console.error('[CognitoService] Verify error:', error);
      throw new UnauthorizedException('Token is invalid');
    }
  }
}
