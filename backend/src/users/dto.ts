export class CreateProfileDto {
  displayName: string;
  gender: string;
  age: number;
  occupation: string;
  avatarUrl?: string;
  needType?: string;
  lifestyle?: string;
  budget?: string;
  location?: string;
  traits?: string;
  bio?: string;
}

export class PresignedUrlDto {
  fileName: string;
  contentType: string;
}
